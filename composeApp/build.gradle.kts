plugins {
    alias(libs.plugins.androidApp)
    alias(libs.plugins.googleServices)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-ui"))
            implementation(project(":core:core-domain"))
            implementation(project(":feature:feature-auth"))
            implementation(project(":core:core-notifications"))
            implementation(project(":core:core-data"))
            implementation(project(":feature:feature-onboarding"))
            implementation(project(":feature:feature-dashboard"))
            implementation(project(":feature:feature-addiction"))
            implementation(project(":feature:feature-checkin"))
            implementation(project(":feature:feature-progress"))
            implementation(project(":feature:feature-sos"))
            implementation(project(":feature:feature-settings"))
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.navigation.compose) // KMP Navigation
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.material.icons.extended)
        }
    }
}