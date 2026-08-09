import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktlint)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(projects.advancedMenubarAwt)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.components.resources)
    implementation(libs.material.symbols)
    implementation(compose.desktop.currentOs)
}

compose.resources {
    packageOfResClass = "composeadvancedmenubar.sample.awt.generated.resources"
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Advanced-Menubar Sample"
            packageVersion = "1.0.0"
            jvmArgs += "--enable-native-access=ALL-UNNAMED"
            macOS {
                bundleID = "dev.hansholz.advancedmenubar.sample.awt"
                dockName = "Advanced-Menubar Sample"
            }
        }

        buildTypes.release.proguard {
            isEnabled = true
            obfuscate = true
            optimize = true
            configurationFiles.from(project.file("src/main/compose-desktop.pro"))
        }
    }
}
