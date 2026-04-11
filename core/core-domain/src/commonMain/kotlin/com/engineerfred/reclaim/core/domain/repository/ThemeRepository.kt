package com.engineerfred.reclaim.core.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {
    val isDarkThemeFlow: StateFlow<Boolean>
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}