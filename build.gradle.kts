// Root build file. Convention plugins handle all module config.
// Only declare plugin versions here that are NOT in build-logic.
plugins {
    alias(libs.plugins.androidApplication)    apply false
    alias(libs.plugins.androidLibrary)        apply false
    alias(libs.plugins.composeMultiplatform)  apply false
    alias(libs.plugins.composeCompiler)       apply false
    alias(libs.plugins.kotlinMultiplatform)   apply false
    alias(libs.plugins.sqldelight)            apply false
    alias(libs.plugins.googleServices)        apply false
}