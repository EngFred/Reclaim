package com.engineerfred.reclaim.core.data.theme

expect class ThemeStorage {
    fun isDarkTheme(): Boolean
    fun setDarkTheme(enabled: Boolean)
}