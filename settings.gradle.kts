pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "Reclaim"

// ── App shell ─────────────────────────────────────────────────────────────────
include(":composeApp")

// ── Core modules ──────────────────────────────────────────────────────────────
include(":core:core-domain")
include(":core:core-data")
include(":core:core-ui")
include(":core:core-notifications")

// ── Feature modules ───────────────────────────────────────────────────────────
include(":feature:feature-auth")
include(":feature:feature-onboarding")
include(":feature:feature-dashboard")
include(":feature:feature-addiction")
include(":feature:feature-checkin")
include(":feature:feature-progress")
include(":feature:feature-sos")
include(":feature:feature-settings")