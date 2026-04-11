package com.engineerfred.reclaim.core.data.theme

import android.content.Context
import androidx.core.content.edit

actual class ThemeStorage(private val context: Context) {

    private val prefs = context.getSharedPreferences("reclaim_prefs", Context.MODE_PRIVATE)

    actual fun isDarkTheme(): Boolean = prefs.getBoolean(KEY_DARK_THEME, false)

    actual fun setDarkTheme(enabled: Boolean) {
        prefs.edit { putBoolean(KEY_DARK_THEME, enabled) }
    }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }
}