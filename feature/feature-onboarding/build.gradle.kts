plugins { alias(libs.plugins.kmpFeature) }

android {
    namespace = "com.engineerfred.reclaim.feature.onboarding"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-domain"))
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-notifications"))
            implementation(libs.koin.compose.viewmodel)
        }
    }
}