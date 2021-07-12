package com.github.daniellfalcao.test._module

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import org.koin.test.KoinTest

open class BaseKoinTest : KoinTest {

    val application: Application = ApplicationProvider.getApplicationContext()

}