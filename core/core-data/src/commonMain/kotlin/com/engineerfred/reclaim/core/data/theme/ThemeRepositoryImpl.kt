package com.engineerfred.reclaim.core.data.theme

import com.engineerfred.reclaim.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeRepositoryImpl(
    private val themeStorage: ThemeStorage
) : ThemeRepository {

    private val _isDarkThemeFlow = MutableStateFlow(themeStorage.isDarkTheme())
    override val isDarkThemeFlow: StateFlow<Boolean> = _isDarkThemeFlow.asStateFlow()

    override fun isDarkTheme(): Boolean = themeStorage.isDarkTheme()

    override fun setDarkTheme(enabled: Boolean) {
        themeStorage.setDarkTheme(enabled)
        _isDarkThemeFlow.value = enabled   // ← notify all observers immediately
    }
}