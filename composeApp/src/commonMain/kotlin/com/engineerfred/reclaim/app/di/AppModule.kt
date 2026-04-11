package com.engineerfred.reclaim.app.di

import com.engineerfred.reclaim.app.AppThemeViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::AppThemeViewModel)
}