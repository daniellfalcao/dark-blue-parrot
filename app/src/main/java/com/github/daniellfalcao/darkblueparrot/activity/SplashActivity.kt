package com.github.daniellfalcao.darkblueparrot.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.github.daniellfalcao.common_ui.extension.observe
import com.github.daniellfalcao.common_ui.ui.activity.ParrotActivity
import com.github.daniellfalcao.darkblueparrot.databinding.ActivitySplashBinding
import com.github.daniellfalcao.darkblueparrot.viewmodel.SplashViewModel
import com.github.daniellfalcao.darkblueparrot.viewmodel.action.SplashAction
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class SplashActivity : ParrotActivity<ActivitySplashBinding>() {

    private val viewModel by viewModel<SplashViewModel>()

    override fun onCreateViewBinding(inflater: LayoutInflater) {
        binding = ActivitySplashBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
        // setup observers
        lifecycleScope.launchWhenStarted {
            viewModel.action.observe(this@SplashActivity) { action ->
                when (action) {
                    is SplashAction.Action.ContinueToNavigation -> {
                        delay(TimeUnit.SECONDS.toMillis(1))
                        NavigationActivity.newIntent(
                            action.hasUserAuthenticated,
                            this@SplashActivity
                        ).also { startActivity(it) }
                    }
                }
            }
        }
        // dispatch view model
        viewModel.dispatchNavigation()
    }


}