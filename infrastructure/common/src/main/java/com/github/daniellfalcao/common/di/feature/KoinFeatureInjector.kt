package com.github.daniellfalcao.common.di.feature

import com.github.daniellfalcao.common.di.feature.authentication.AuthenticationFeatureDI
import com.github.daniellfalcao.common.di.feature.bookmark.BookmarkFeatureDI
import com.github.daniellfalcao.common.di.feature.home.HomeFeatureDI
import com.github.daniellfalcao.common.di.feature.profile.ProfileFeatureDI
import java.util.ServiceLoader

object KoinFeatureInjector {

    private var isAuthenticationModuleInjected = false
    private var isBookmarkModuleInjected = false
    private var isHomeModuleInjected = false
    private var isProfileModuleInjected = false

    fun attemptInjectModules() {
        if (!isAuthenticationModuleInjected) {
            withFeature<AuthenticationFeatureDI> {
                inject()
                isAuthenticationModuleInjected = true
            }
        }
        if (!isBookmarkModuleInjected) {
            withFeature<BookmarkFeatureDI> {
                inject()
                isBookmarkModuleInjected = true
            }
        }
        if (!isHomeModuleInjected) {
            withFeature<HomeFeatureDI> {
                inject()
                isHomeModuleInjected = true
            }
        }
        if (!isProfileModuleInjected) {
            withFeature<ProfileFeatureDI> {
                inject()
                isProfileModuleInjected = true
            }
        }
    }

    private inline fun <reified T> withFeature(block: T.() -> Unit) {
        try {
            ServiceLoader.load(T::class.java).iterator().next()?.apply(block)
        } catch (e: Exception) {
        }
    }
}