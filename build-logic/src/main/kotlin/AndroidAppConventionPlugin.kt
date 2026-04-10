import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for the Android application shell (composeApp).
 */
class AndroidAppConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.application")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = 36
                namespace  = "com.engineerfred.reclaim"
                defaultConfig {
                    applicationId = "com.engineerfred.reclaim"
                    minSdk        = 24
                    targetSdk     = 36
                    versionCode   = 1
                    versionName   = "1.0"
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_11
                    targetCompatibility = JavaVersion.VERSION_11
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
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
                listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
                    iosTarget.binaries.framework {
                        baseName = "ComposeApp"
                        isStatic = true
                    }
                }

                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(compose.runtime)
                        implementation(compose.foundation)
                        implementation(compose.material3)
                        implementation(compose.ui)
                        implementation(compose.components.resources)
                        implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.3")

                        implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                        implementation("io.insert-koin:koin-core:$koinVersion")
                        implementation("io.insert-koin:koin-compose:$koinVersion")
                    }

                    androidMain.dependencies {
                        implementation("androidx.activity:activity-compose:1.13.0")
                        implementation(compose.uiTooling)
                        implementation("io.insert-koin:koin-android:$koinVersion")
                        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
                    }
                }
            }
        }
    }
}