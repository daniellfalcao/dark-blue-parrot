package com.github.daniellfalcao.common_ui.ui.binding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

interface Binding<VB : ViewBinding> {

    var binding: VB?

    /**
     * Generates the [ViewBinding] instance.
     *
     *      Example:
     *
     *      fun onCreateViewBinding(inflater: LayoutInflater): VB? {
     *          binding = ViewBinding.inflate(inflater)
     *      }
     *
     * **/
    fun onCreateViewBinding(inflater: LayoutInflater) {
        throw NotImplementedError("onCreateViewBinding must be implemented")
    }

    /**
     * Generates the [ViewBinding] instance.
     *
     *      Example:
     *
     *      fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB? {
     *          binding = ViewBinding.inflate(inflater, container, container != null)
     *      }
     *
     * **/
    fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        throw NotImplementedError("onCreateViewBinding must be implemented")
    }

    /**
     * Generates the [ViewBinding] instance.
     *
     *      Example:
     *
     *      fun onCreateViewBinding(view: View): VB? {
     *          binding = ViewBinding.bind(view)
     *      }
     *
     * **/
    fun onCreateViewBinding(view: View) {
        throw NotImplementedError("onCreateViewBinding must be implemented")
    }

}

fun <VB : ViewBinding> Binding<VB>.withBinding(action: VB.() -> Unit) {
    binding?.let(action)
}