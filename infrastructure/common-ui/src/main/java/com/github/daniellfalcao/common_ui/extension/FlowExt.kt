package com.github.daniellfalcao.common_ui.extension

import androidx.lifecycle.LifecycleOwner
import com.github.daniellfalcao.common_ui.utilities.FlowLifecycleObserver
import kotlinx.coroutines.flow.Flow

inline fun <reified T> Flow<T>.observe(
    lifecycleOwner: LifecycleOwner,
    noinline collector: suspend (T) -> Unit
) {
    FlowLifecycleObserver(lifecycleOwner, this, collector)
}