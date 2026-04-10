import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 36
                defaultConfig {
                    minSdk = 24
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                androidTarget {
                    compilerOptions {
                        jvmTarget.set(JvmTarget.JVM_17)
                        freeCompilerArgs.add("-Xexpect-actual-classes")
                    }
                }
                iosArm64()
                iosSimulatorArm64()

                sourceSets.apply {
                    commonMain.dependencies {
                        implementation(
                            target.dependencies.create(
                                "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2"
                            )
                        )
                    }
                    commonTest.dependencies {
                        implementation(kotlin("test"))
                        implementation(
                            target.dependencies.create(
                                "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2"
                            )
                        )
                    }
                }
            }
        }
    }
}