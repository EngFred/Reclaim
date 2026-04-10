import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for a KMP feature module.
 * Includes Compose Multiplatform + ViewModel + Koin.
 * Used by: all feature-* modules and core-ui.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 36
                defaultConfig { minSdk = 24 }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
            }

            // Get compose dependencies AFTER the compose plugin is applied
            val compose = extensions
                .getByType(ComposeExtension::class.java)
                .dependencies

            val coroutinesVersion = "1.10.2"
            val koinVersion       = "4.2.1"
            val lifecycleVersion  = "2.10.0"

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_11)
                    }
                }
                iosArm64()
                iosSimulatorArm64()

                sourceSets.apply {
                    commonMain.dependencies {
                        // Compose — resolved by the plugin, correct versions automatically
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.3")

                        // Lifecycle
                        implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
                        implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")

                        // Coroutines
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                        // Koin
                        implementation("io.insert-koin:koin-core:$koinVersion")
                        implementation("io.insert-koin:koin-compose:$koinVersion")
                        implementation("io.insert-koin:koin-compose-viewmodel:$koinVersion")
                    }

                    androidMain.dependencies {
                        implementation(compose.uiTooling)
                    }

                    commonTest.dependencies {
                        implementation(kotlin("test"))
                        implementation("app.cash.turbine:turbine:1.2.1")
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
                    }
                }
            }
        }
    }
}