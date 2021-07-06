package com.github.daniellfalcao.darkblueparrot

import android.os.Bundle
import android.view.LayoutInflater
import com.github.daniellfalcao.common_ui.ui.activity.ParrotActivity
import com.github.daniellfalcao.darkblueparrot.databinding.ActivitySplashBinding

class SplashActivity : ParrotActivity<ActivitySplashBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater) {
        binding = ActivitySplashBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }

}