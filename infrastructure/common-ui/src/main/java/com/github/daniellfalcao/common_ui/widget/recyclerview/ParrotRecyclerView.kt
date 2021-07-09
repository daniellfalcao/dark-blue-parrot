package com.github.daniellfalcao.common_ui.widget.recyclerview

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.github.daniellfalcao.common.extension.doOnSuspendLock
import com.github.daniellfalcao.common.extension.safeUnlock
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.CoroutineContext

open class ParrotRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var onLoadMore: (() -> Unit)? = null

    init {
        itemAnimator = ParrotItemAnimator()
    }

    /**
     * Set a callback to load more behavior.
     *
     * When the recycler view scrolls to the end of list, it will call the assigned lambda.
     *
     * */
    fun onLoadMore(callback: (() -> Unit)) {
        onLoadMore = callback
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        // Only accept adapter who is instance of ParrotRecyclerView.Adapter
        if (adapter != null && adapter !is Adapter<*, *>) {
            throw IllegalArgumentException("This RecyclerView is only used with ParrotRecyclerView.Adapter")
        }
        super.setAdapter(adapter)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        // if recycler view can't scroll anymore to the down and is not performing a scrolling action
        // it will call onLoadMore
        if (!canScrollVertically(SCROLLING_DOWN_DIRECTION) && state == SCROLL_STATE_IDLE) {
            onLoadMore?.invoke()
        }
    }

    abstract class ViewHolder<T : Any, VB : ViewBinding>(
        protected val binding: VB
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun bind(data: T) {
        }

        open fun unbind() {
        }

        protected val context: Context
            get() = itemView.context

        protected val resources: Resources
            get() = context.resources

        fun withBinding(action: VB.() -> Unit) {
            binding.run(action)
        }
    }

    abstract class Adapter<T : Any, VH : ViewHolder<T, *>> : RecyclerView.Adapter<VH>,
        CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = SupervisorJob() + Dispatchers.Main

        private var enableDiffUtil: Boolean = false
        private lateinit var diffUtilBuilder: DiffUtilBuilder

        /**
         * The refresh thread blocker used by [DiffUtil].
         *
         * */
        private val refreshOperationManager = Mutex(false)

        /**
         * The list of pending data to be updated in the recycler view.
         *
         * All pending data not processed by [DiffUtil] will be contained in this list waiting the
         * [DiffUtil] completing the processing.
         *
         * */
        private var pendingRefreshData = mutableListOf<List<T>>()

        /**
         * The current displayed items in recycler view.
         *
         * */
        private var _items = mutableListOf<T>()
        val items: List<T> = _items

        /**
         * A reference to recycler view that's holders this adapter.
         *
         * */
        protected var recyclerView: RecyclerView? = null

        /**
         * Call this constructor if the desired behavior is just load items without using [DiffUtil]
         *
         * */
        constructor() : super()

        /**
         * Call this constructor if the desired behavior is load items using a [DiffUtil].
         *
         * @param diffUtilBuilder it's a [DiffUtil.Callback] generator to be used to calculate the
         *                        difference between 2 lists.
         *
         * */
        constructor(diffUtilBuilder: DiffUtilBuilder) : super() {
            this.enableDiffUtil = true
            this.diffUtilBuilder = diffUtilBuilder
        }

        /**
         * Refresh [items] in recycler view.
         *
         * If diffUtil is enable, perform the calculation of changes of each element in the old list
         * displayed by the recycler view.
         *
         * If diffUtil is not enable, just clear old list and update the items.
         *
         * */
        open fun refresh(items: List<T>) {
            pendingRefreshData.add(items)
            if (enableDiffUtil) {
                refreshWithDiffUtil()
            } else {
                refreshWithoutDiffUtil()
            }
        }

        /**
         * Returns a element in the given [position].
         *
         * */
        open fun getItemAt(position: Int): T? {
            return try {
                _items[position]
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }

        /**
         * Perform the update operation using [DiffUtil].
         *
         * */
        protected open fun refreshWithDiffUtil() {

            /**
             * Calculate the differences between [_items] and [newItems] and dispatch it to adapter
             *
             * if [pendingRefreshData] is empty.
             *
             * If [pendingRefreshData] is not empty call [refreshWithDiffUtil] again to recalculate
             * differences.
             *
             * */
            suspend fun update(newItems: List<T>) {
                val callback = diffUtilBuilder.build(newItems, _items)
                // calculate the deference between lists.
                val difference = DiffUtil.calculateDiff(callback, true)
                // in the main dispatcher, dispatch the list changes in the adapter.
                withContext(Dispatchers.Main) {
                    if (pendingRefreshData.isEmpty()) {
                        _items.clear()
                        _items.addAll(newItems)
                        // save current layout manager state to avoid scroll to the top
                        val state = recyclerView?.layoutManager?.onSaveInstanceState()
                        difference.dispatchUpdatesTo(this@Adapter)
                        // after dispatch updates to the recyclerView restore state to keep
                        // scroll state
                        recyclerView?.layoutManager?.onRestoreInstanceState(state)
                        refreshOperationManager.safeUnlock()
                        refreshWithDiffUtil()
                    } else {
                        refreshOperationManager.safeUnlock()
                        refreshWithDiffUtil()
                    }
                }
            }
            // attempt start the refresh operation if the mutex is not blocked.
            if (!refreshOperationManager.isLocked && pendingRefreshData.isNotEmpty()) {
                launch(Dispatchers.Default) {
                    refreshOperationManager.doOnSuspendLock {
                        update(pendingRefreshData.removeFirst())
                    }
                }
            }
        }

        /**
         * Perform the update operation without use diffUtil. Just replace data and notify.
         *
         * */
        protected open fun refreshWithoutDiffUtil() {
            if (pendingRefreshData.isNotEmpty()) {
                this._items.clear()
                this._items.addAll(pendingRefreshData.removeFirst())
            }
            notifyDataSetChanged()
        }

        /**
         * Return the current size of item list displayed in the recycler view.
         *
         * */
        override fun getItemCount() = _items.size

        /**
         * Save the recycler view instance.
         *
         * */
        override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
            super.onAttachedToRecyclerView(recyclerView)
            this.recyclerView = recyclerView
        }

        /**
         * Release recyclerView instance.
         *
         * */
        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
            super.onDetachedFromRecyclerView(recyclerView)
            this.recyclerView = null
        }

        /**
         * Unbind view holder when its recycled.
         *
         * */
        override fun onViewRecycled(holder: VH) {
            super.onViewRecycled(holder)
            holder.unbind()
        }
    }

    companion object {
        const val SCROLLING_DOWN_DIRECTION = 1
    }

}