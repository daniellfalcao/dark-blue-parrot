package com.github.daniellfalcao.common.di.feature.authentication

import org.koin.core.module.Module

interface AuthenticationFeatureDI {
    fun inject(): Module
}