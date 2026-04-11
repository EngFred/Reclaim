plugins { alias(libs.plugins.kmpFeature) }

android {
    namespace = "com.engineerfred.reclaim.core.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-domain"))
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}