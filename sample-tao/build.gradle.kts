
import dev.nucleusframework.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.nucleus)
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
    implementation(projects.advancedMenubarTao)
    implementation(libs.compose.material3)
    implementation(libs.compose.components.resources)
    implementation(libs.material.symbols)
    implementation(libs.nucleus.graalvm.runtime)
    implementation(compose.desktop.currentOs)
}

compose.resources {
    packageOfResClass = "composeadvancedmenubar.sample.tao.generated.resources"
}

nucleus.application {
    mainClass = "MainKt"

    nativeDistributions {
        targetFormats(TargetFormat.Dmg)
        packageName = "Advanced-Menubar Tao Sample"
        packageVersion = "1.0.0"

        jvmArgs += "--enable-native-access=ALL-UNNAMED"
        if (System.getProperty("os.name").startsWith("Mac")) {
            jvmArgs += "-XstartOnFirstThread"
        }

        macOS {
            bundleID = "dev.hansholz.advancedmenubar.sample.tao"
            dockName = "Advanced-Menubar Tao Sample"
        }
    }

    buildTypes.release.proguard {
        isEnabled = true
        obfuscate = true
        optimize = true
        configurationFiles.from(project.file("src/main/compose-desktop.pro"))
    }

    graalvm {
        isEnabled = true
    }
}
