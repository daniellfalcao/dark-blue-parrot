package com.github.daniellfalcao.authentication.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.github.daniellfalcao.common_ui.extension.compatDrawable
import com.github.daniellfalcao.common_ui.model.Parrot
import com.github.daniellfalcao.common_ui.widget.recyclerview.ParrotRecyclerView
import com.github.daniellfalcao.darkblueparrot.authentication.R
import com.github.daniellfalcao.darkblueparrot.authentication.databinding.VhParrotBinding
import timber.log.Timber

class ParrotSelectorList @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ParrotRecyclerView(context, attrs, defStyleAttr) {

    var selectedParrot: Parrot

    private val snapHelper = PagerSnapHelper()
    private val parrotList = Parrot.values().toList()

    init {
        // set current parrot
        selectedParrot = parrotList.first()
        // configure parrot list
        layoutManager = LinearLayoutManager(context, HORIZONTAL, false)
        snapHelper.attachToRecyclerView(this)
        adapter = Adapter().apply { refresh(parrotList) }
        overScrollMode = OVER_SCROLL_NEVER
        clipToPadding = false
        // configure padding to align first and last item to the center of screen\
        val itemSize = context.resources.getDimensionPixelSize(R.dimen.parrot_list_item_size)
        val listWidth = context.resources.displayMetrics.widthPixels
        val centerPadding = (listWidth / 2) - (itemSize / 2)
        setPadding(centerPadding, paddingTop, centerPadding, paddingBottom)
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (state == SCROLL_STATE_IDLE) {
            snapHelper.findSnapView(layoutManager)?.let { currentSelectedView ->
                layoutManager?.getPosition(currentSelectedView)
            }?.let { currentSelectedViewPosition ->
                (adapter as? Adapter)?.getItemAt(currentSelectedViewPosition)
            }?.let { parrot ->
                selectedParrot = parrot
                Timber.i("---> selected parrot = $parrot")
            }
        }
    }

    fun selectParrot(parrot: Parrot) {
        scrollToPosition(parrotList.indexOf(parrot))
    }

    private class Adapter : ParrotRecyclerView.Adapter<Parrot, ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.newInstance(parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            getItemAt(position)?.let { holder.bind(it) }
        }

    }

    private class ViewHolder(
        binding: VhParrotBinding
    ) : ParrotRecyclerView.ViewHolder<Parrot, VhParrotBinding>(binding) {

        override fun bind(data: Parrot) = withBinding {
            parrot.setImageDrawable(context.compatDrawable(data.drawableRes))
        }

        companion object {
            fun newInstance(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VhParrotBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}