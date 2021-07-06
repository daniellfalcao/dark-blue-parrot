package com.github.daniellfalcao.common_ui.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.github.daniellfalcao.common_ui.ui.binding.Binding

abstract class ParrotActivity<VB: ViewBinding> : AppCompatActivity(), Binding<VB> {

    override var binding: VB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateViewBinding(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}