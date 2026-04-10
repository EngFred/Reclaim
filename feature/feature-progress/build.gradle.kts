plugins { alias(libs.plugins.kmpFeature) }

android {
    namespace = "com.engineerfred.reclaim.feature.progress"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-domain"))
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-data"))
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.sqldelight.coroutines)
        }
    }
}