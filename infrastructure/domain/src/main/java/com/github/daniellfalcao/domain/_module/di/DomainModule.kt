package com.github.daniellfalcao.domain._module.di

import com.github.daniellfalcao.common.di.KoinModuleDI
import com.github.daniellfalcao.domain.user.usecase.CheckAuthenticationStateUseCase
import org.koin.dsl.module

object DomainModule : KoinModuleDI {

    override fun inject() = module {
        factory { CheckAuthenticationStateUseCase(get()) }
    }

}