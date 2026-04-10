plugins {
    alias(libs.plugins.androidApp)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Add your project-specific dependencies here
            implementation(project(":core:core-data"))

            // You will likely need to add your feature modules here too
            // so the app shell can navigate to them and wire up Koin!
            // implementation(project(":feature:feature-auth"))
            // implementation(project(":feature:feature-dashboard"))
            // etc...
        }
    }
}