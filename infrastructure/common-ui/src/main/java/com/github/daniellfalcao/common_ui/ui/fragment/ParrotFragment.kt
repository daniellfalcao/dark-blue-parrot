package com.github.daniellfalcao.common_ui.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.github.daniellfalcao.common.di.feature.KoinFeatureInjector
import com.github.daniellfalcao.common_ui.ui.binding.Binding

abstract class ParrotFragment<VB : ViewBinding>(
    private val showOptionsMenu: Boolean = false
) : Fragment(), Binding<VB> {

    override var binding: VB? = null

    abstract override fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // inject lazy features modules
        KoinFeatureInjector.attemptInjectModules()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(showOptionsMenu)
        onCreateViewBinding(inflater, container)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}