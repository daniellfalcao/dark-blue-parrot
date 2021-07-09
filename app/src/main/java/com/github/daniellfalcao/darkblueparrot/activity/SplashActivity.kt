package com.github.daniellfalcao.darkblueparrot.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.github.daniellfalcao.common_ui.ui.activity.ParrotActivity
import com.github.daniellfalcao.darkblueparrot.databinding.ActivitySplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ParrotActivity<ActivitySplashBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater) {
        binding = ActivitySplashBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        lifecycleScope.launch {
            delay(2000)
            Intent(this@SplashActivity, NavigationActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}