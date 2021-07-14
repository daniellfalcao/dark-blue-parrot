package com.github.daniellfalcao.darkblueparrot.application

import com.github.daniellfalcao.darkblueparrot.BuildConfig
import com.github.daniellfalcao.darkblueparrot._module.di.AppModule
import com.github.daniellfalcao.data._module.di.DataModule
import com.github.daniellfalcao.domain.user._module.di.DomainModule
import com.google.android.play.core.splitcompat.SplitCompatApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ParrotApplication : SplitCompatApplication() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@ParrotApplication)
            modules(
                DataModule.inject(),
                DomainModule.inject(),
                AppModule.inject()
            )
        }
    }
}