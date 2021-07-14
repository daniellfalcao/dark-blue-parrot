package com.github.daniellfalcao.common_ui.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.github.daniellfalcao.common_ui.ui.binding.Binding

abstract class ParrotActivity<VB : ViewBinding> : AppCompatActivity(), Binding<VB> {

    override var binding: VB? = null

    abstract override fun onCreateViewBinding(inflater: LayoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateViewBinding(layoutInflater)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}

/**
 * Retrieve the extra from activity contained in the [key]. If don't have a value (null) to key,
 * returns a default value.
 *
 * */
inline fun <reified T : Any> ParrotActivity<*>.extra(key: String, default: T) = lazy {
    intent.extras?.get(key) as? T ?: default
}
