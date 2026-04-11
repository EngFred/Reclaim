package com.engineerfred.reclaim.core.data.theme

import platform.Foundation.NSUserDefaults

actual class ThemeStorage {

    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun isDarkTheme(): Boolean = defaults.boolForKey(KEY_DARK_THEME)

    actual fun setDarkTheme(enabled: Boolean) {
        defaults.setBool(enabled, KEY_DARK_THEME)
    }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }
}