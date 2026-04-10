plugins { alias(libs.plugins.kmpLibrary) }

android {
    namespace = "com.engineerfred.reclaim.core.notifications"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:core-domain"))
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.workmanager)
            implementation(libs.firebase.messaging)
            implementation(libs.coroutines.android)
            implementation(libs.androidx.core.ktx)          // ← NotificationCompat
            implementation(libs.koin.android)               // ← androidContext()
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}