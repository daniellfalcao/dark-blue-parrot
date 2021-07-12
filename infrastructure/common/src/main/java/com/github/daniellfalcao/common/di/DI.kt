package com.github.daniellfalcao.common.di

import org.koin.core.module.Module

interface DI {
    fun koinModule(): Module
}