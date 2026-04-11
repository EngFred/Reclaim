package com.engineerfred.reclaim.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engineerfred.reclaim.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AppThemeViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = themeRepository.isDarkThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = themeRepository.isDarkTheme()
        )

    fun setDarkTheme(enabled: Boolean) {
        themeRepository.setDarkTheme(enabled)
    }
}