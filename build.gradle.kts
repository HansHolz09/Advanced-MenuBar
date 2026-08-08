import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.mavenPublishVanniktech) apply false
    alias(libs.plugins.ktlint) apply false
}

subprojects {
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
        tasks.withType<BaseKtLintCheckTask>().configureEach {
            exclude("**/generated/resources/**")
        }
    }
}
