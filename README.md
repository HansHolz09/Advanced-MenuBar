# Advanced Menubar

## Overview

Advanced Menubar provides a declarative Compose DSL for native macOS menus. It supports standard AppKit handlers, Services, Window and Help menus, Compose-compatible Edit commands, Writing Tools, AutoFill, Dictation, native text context menus, SF Symbols, custom icons, shortcuts, checked items, subtitles, tooltips and badges. Choose the AWT/Swing backend for Compose Desktop windows or the macOS Tao backend for an AWT-free application.

## Quickstart

```kotlin
dependencies {
    implementation("dev.hansholz:advanced-menubar-awt:0.1.0-alpha06")
}
```

```kotlin
Window(onCloseRequest = ::exitApplication, title = "Example") {
    DefaultMacMenuBar(
        onAboutClick = { showAbout() },
        onSettingsClick = { showSettings() },
    )

    AppContent()
}
```

For Tao use `dev.hansholz:advanced-menubar-tao:0.1.0-alpha06` and call the same `DefaultMacMenuBar` inside a `TaoDecoratedWindowScope`. See the [documentation](docs/index.md) for custom menus, text context menus, backend setup and GraalVM packaging.
