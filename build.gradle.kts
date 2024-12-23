plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
}

repositories {
    mavenCentral()
}

kotlin {
    mingwX64()
    linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.framework.datatest)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
