# Quickstart

Replace `current-version` in the examples with the current published release.

## Compose Desktop Window

```kotlin
dependencies {
    implementation("dev.hansholz:advanced-menubar-awt:current-version")
}
```

```kotlin
Window(onCloseRequest = ::exitApplication, title = "Notes") {
    DefaultMacMenuBar(
        onAboutClick = { showAbout = true },
        onSettingsClick = { showSettings = true },
        onHelpClick = ::openHelp,
    )
    NotesScreen()
}
```

Use `AdvancedMacMenuBar` for a custom native macOS structure, `SwingMenuBar` for an explicit Swing
menu, or `CompatibilityMenuBar` to choose AppKit on macOS and Swing on Windows/Linux.

## Nucleus Tao window

Tao comes from [Nucleus](https://github.com/NucleusFramework/Nucleus).

```kotlin
dependencies {
    implementation("dev.hansholz:advanced-menubar-tao:current-version")
}
```

```kotlin
taoApplication {
    DecoratedWindow(onCloseRequest = ::exitApplication, title = "Notes") {
        DefaultMacMenuBar(appName = "Notes")
        NotesScreen()
    }
}
```

Both artifacts target JVM 21. Follow the Nucleus documentation for Tao window and application
packaging setup.
