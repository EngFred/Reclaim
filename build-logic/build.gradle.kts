plugins {
    `kotlin-dsl`
}

group = "com.engineerfred.reclaim.buildlogic"

dependencies {
    compileOnly(libs.gradlePlugin.android)
    compileOnly(libs.gradlePlugin.kotlin)
    compileOnly(libs.gradlePlugin.compose)
    compileOnly(libs.gradlePlugin.composeCompiler)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "reclaim.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpFeature") {
            id = "reclaim.kmp.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
        register("androidApp") {
            id = "reclaim.android.app"
            implementationClass = "AndroidAppConventionPlugin"
        }
    }
}