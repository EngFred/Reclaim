plugins { alias(libs.plugins.kmpLibrary) }

android {
    namespace = "com.engineerfred.reclaim.core.domain"
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.coroutines.test)
            implementation(libs.turbine)
        }
    }
}