# Custom and dynamic menus

The DSL is rebuilt from current Compose state, so ordinary Kotlin conditions and loops are enough:

```kotlin
AdvancedMacMenuBar(appName = "Notes") {
    FileMenu {
        FileNew { newDocument() }
        FileOpenRecent {
            recentFiles.forEach { file ->
                Item(file.name, icon = MenuIcon.File(file.iconPath)) { open(file) }
            }
            Separator()
            FileClearRecent(enabled = recentFiles.isNotEmpty()) { recentFiles.clear() }
        }
    }
    CustomMenu("Options") {
        Section("Workspace") {
            Checkbox("Autosave", autosave) { autosave = it }
            Menu("Theme", subtitle = "Current: ${theme.label}", badge = updateBadge) {
                Item("Light") { theme = Theme.Light }
                Item("Dark") { theme = Theme.Dark }
            }
        }
    }
    if (developerMode) CustomMenu("Developer") { Item("Inspect state", onClick = ::inspect) }
}
```

AppKit receives subtitles and badges for custom items, checkboxes, and submenus. Swing intentionally
omits those two properties. Tooltips are supported for action and checkbox items. Sections retain
their surrounding separators; native separator presentation is left to macOS.

`EditMenu(suppressAutomaticItems = true)` removes modern AppKit text-service entries.
`WindowMenu(suppressAutoWindowList = true)` prevents AppKit from appending its window list.

Recomposition updates callbacks even when the encoded native menu is unchanged. Structural or
visual changes replace the installed native menu for the currently active window.
