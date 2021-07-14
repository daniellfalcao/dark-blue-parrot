package com.github.daniellfalcao.common.di

import org.koin.core.module.Module

interface KoinModuleDI {
    fun inject(): Module
}