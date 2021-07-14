package com.github.daniellfalcao.darkblueparrot.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.navigation.Navigation
import com.github.daniellfalcao.common_ui.ui.activity.ParrotActivity
import com.github.daniellfalcao.common_ui.ui.activity.extra
import com.github.daniellfalcao.common_ui.ui.binding.withBinding
import com.github.daniellfalcao.darkblueparrot.R
import com.github.daniellfalcao.darkblueparrot.databinding.ActivityNavigationBinding


class NavigationActivity : ParrotActivity<ActivityNavigationBinding>() {

    private val hasUserAuthenticated by extra(EXTRA_HAS_USER_AUTHENTICATED, false)

    override fun onCreateViewBinding(inflater: LayoutInflater) {
        binding = ActivityNavigationBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        withBinding {
            val navController = Navigation.findNavController(navHostFragment)
            val graph = navController.navInflater.inflate(R.navigation.nav_app_graph).apply {
                startDestination = if (hasUserAuthenticated) {
                    R.id.homeFragment
                } else {
                    R.id.preSignFragment
                }
            }
            navController.graph = graph
        }
    }

    companion object {

        private const val EXTRA_HAS_USER_AUTHENTICATED = "EXTRA_HAS_USER_AUTHENTICATED"

        fun newIntent(
            hasUserAuthenticated: Boolean,
            context: Context
        ) = Intent(context, NavigationActivity::class.java).apply {
            putExtra(EXTRA_HAS_USER_AUTHENTICATED, hasUserAuthenticated)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}