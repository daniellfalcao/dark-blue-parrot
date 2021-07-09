package com.github.daniellfalcao.darkblueparrot

import com.google.android.play.core.splitcompat.SplitCompatApplication
import timber.log.Timber

class ParrotApplication : SplitCompatApplication() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}