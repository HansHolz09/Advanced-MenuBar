# Edit commands and text context menus

## Native Edit menu

AppKit normally dispatches Edit selectors through its responder chain, while Compose owns text input
inside Skia. Callback-free standard Edit entries therefore use a small key-event bridge so clicks
and Command accelerators reach the focused Compose field:

```kotlin
EditMenu {
    Undo()
    Redo()
    Separator()
    Cut()
    Copy()
    Paste()
    PasteAndMatchStyle()
    Delete()
    SelectAll()
    Separator()
    FindMenu {
        Find()
        FindAndReplace()
        FindNext()
        FindPrevious()
    }
}
```

Supply callbacks when your editor owns a separate document model. In Swing, callbacks are required;
there is no AppKit/Compose bridge, and callback-free actions are disabled.

## Native right-click menu

```kotlin
NativeTextContextMenuProvider(
    isDark = darkTheme,
    showExtraOptions = true,
    customActions = listOf(
        ContextMenuAction("Look up in project", "magnifyingglass") { lookUp() },
    ),
) {
    OutlinedTextField(state = textState)
}
```

`showExtraOptions = false` keeps a compact Cut/Copy/Paste menu plus Select All and custom actions.
Text transformations temporarily use the system pasteboard to return AppKit's edited value to
Compose. All original pasteboard item formats are restored afterward, and restoration is skipped if
the user or another application changed the pasteboard in the meantime.

`isDark` changes the appearance of the entire `NSApplication`; this is intentional so every native
menu follows the application theme. Pass `null` to keep the current AppKit appearance. When the JNI
bridge cannot load, Compose's original text context menu remains active.

<img alt="Native right-click menu on macOS" src="/assets/text-context.png" style="border-radius:15px"/>
