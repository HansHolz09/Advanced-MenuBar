enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ComposeAdvancedMenubar"
include(":advanced-menubar-core")
include(":advanced-menubar-awt")
include(":advanced-menubar-tao")
include(":sample-awt")
include(":sample-tao")
