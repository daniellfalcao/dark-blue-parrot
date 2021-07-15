package com.github.daniellfalcao.authentication._module.di

import com.github.daniellfalcao.authentication.viewmodel.SignUpViewModel
import com.github.daniellfalcao.common.di.feature.authentication.AuthenticationFeatureDI
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AuthenticationFeatureDIImpl : AuthenticationFeatureDI {

    override fun inject() = module {
        viewModel { SignUpViewModel(get(), get(), get()) }
    }

}