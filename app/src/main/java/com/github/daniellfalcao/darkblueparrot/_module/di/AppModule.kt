package com.github.daniellfalcao.darkblueparrot._module.di

import com.github.daniellfalcao.common.di.KoinModuleDI
import com.github.daniellfalcao.darkblueparrot.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AppModule : KoinModuleDI {

    override fun inject() = module {
        viewModel { SplashViewModel(get()) }
    }

}