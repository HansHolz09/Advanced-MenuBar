import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.mavenPublishVanniktech)
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
    api(libs.compose.runtime)
    api(libs.compose.ui)
    api(libs.compose.foundation)
    api(libs.compose.components.resources)
    implementation(libs.nucleus.core.runtime)
}

compose.resources {
    packageOfResClass = "composeadvancedmenubar.generated.resources"
}

val nativeResourceDir = layout.projectDirectory.dir("src/main/resources/advanced-menubar/native")

val buildNativeMacOs by tasks.registering(Exec::class) {
    description = "Compiles the Objective-C JNI bridge for macOS arm64 and x64"
    group = "build"
    enabled = Os.isFamily(Os.FAMILY_MAC)
    val nativeDir = layout.projectDirectory.dir("src/main/native/macos")
    inputs.dir(nativeDir)
    outputs.dir(nativeResourceDir)
    workingDir(nativeDir)
    commandLine("bash", "build.sh")
}

tasks.processResources {
    dependsOn(buildNativeMacOs)
}

tasks.configureEach {
    if (name == "sourcesJar") dependsOn(buildNativeMacOs)
}
