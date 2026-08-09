# Icons, shortcuts, and localization

## Icons

```kotlin
Item("System icon", icon = MenuIcon.SFSymbol("doc.on.doc")) { copy() }
Item("PNG", icon = MenuIcon.Png(pngBytes, template = false)) { export() }
Item("File", icon = MenuIcon.File(iconPath)) { open() }
val vectorIcon = rememberMenuIconFrom(Icons.Default.Settings)
```

Vector icons use at least a 2x backing raster and are assigned the same logical point size as an SF
Symbol by the native renderer. This keeps edges sharp on Retina displays without making Compose
vectors appear larger than adjacent symbols. Auto-mirrored vectors follow the current layout
direction. Template icons are tinted by AppKit; use `template = false` for multicolor artwork. SF
Symbols are macOS-only. Swing supports PNG and file images and omits unsupported symbols.

Subtitles and badges depend on the capabilities of the running macOS release. Do not use them as the
only way to communicate essential state.

## Shortcuts

Custom shortcuts use Compose `Key` values:

```kotlin
Item("Export", MenuShortcut(Key.E, meta = true, shift = true)) { export() }
```

`meta` means Command on macOS. For cross-platform menus, choose modifiers conditionally if the same
custom item should use Ctrl on Windows/Linux. Standard DSL items choose backend conventions for
you: Command-based AppKit shortcuts on macOS and Ctrl/F-key conventions in Swing.

Avoid unmodified letter shortcuts: native menu accelerators can otherwise intercept normal typing.

## Localization

Standard labels ship in every language listed by `MenuBarLanguage`. They follow the process locale;
custom labels remain the application's responsibility. Override any standard `title` when product
wording differs. If an application changes `Locale.getDefault()` at runtime, key the menu subtree by
the selected locale so Compose resources are read again.
