package com.github.daniellfalcao.darkblueparrot

import android.os.Bundle
import android.view.LayoutInflater
import com.github.daniellfalcao.common_ui.ui.activity.ParrotActivity
import com.github.daniellfalcao.darkblueparrot.databinding.ActivityNavigationBinding

class NavigationActivity : ParrotActivity<ActivityNavigationBinding>() {

    override fun onCreateViewBinding(inflater: LayoutInflater) {
        binding = ActivityNavigationBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }

    companion object {
        const val EXTRA_HAS_USER_AUTHENTICATED = "EXTRA_HAS_USER_AUTHENTICATED"
    }
}