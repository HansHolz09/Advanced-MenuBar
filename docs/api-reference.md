# DSL reference

This page is a compact map of the public user-facing DSL. Every standard entry accepts a localized
default title plus the relevant `enabled`, `icon`, state, and callback parameters.

## Top-level menus

| Function | Purpose |
| --- | --- |
| `MacApplicationMenu` | macOS application menu; omitted by Swing |
| `FileMenu` | document/file operations |
| `EditMenu` | editing and optional automatic AppKit services |
| `FormatMenu` | text formatting operations |
| `ViewMenu` | toolbar, sidebar, tab bar, fullscreen |
| `WindowMenu` | window actions and optional AppKit window list |
| `HelpMenu` | application help |
| `CustomMenu` | repeatable application-defined top-level menu |

Only `CustomMenu` may appear more than once. A duplicate standard top-level declaration is ignored.

## Application and File

- Application: `About`, `Settings`, `Services`, `Hide`, `HideOthers`, `ShowAll`, `Quit`.
- File: `FileNew`, `FileOpen`, `FileOpenRecent`, `FileClearRecent`, `FileClose`, `FileCloseAll`,
  `FileSave`, `FileSaveAs`, `FileDuplicate`, `FileRename`, `FileMoveTo`, `FilePageSetup`, `FilePrint`.

`FileOpenRecent` is a submenu builder. Standard actions with no callback use AppKit selectors where
available; Swing disables callback-free actions.

## Edit

- Core: `Undo`, `Redo`, `Cut`, `Copy`, `Paste`, `PasteAndMatchStyle`, `Delete`, `SelectAll`.
- Find: `FindMenu`, `Find`, `FindAndReplace`, `FindNext`, `FindPrevious`,
  `UseSelectionForFind`, `JumpToSelection`.
- Spelling/Substitutions: `SpellingAndGrammarMenu`, `ToggleCorrectSpellingAutomatically`,
  `SubstitutionsMenu`, `ToggleSmartQuotes`, `ToggleSmartDashes`, `ToggleSmartLinks`,
  `ToggleTextReplacement`.
- Transform/Speech: `TransformationsMenu`, `MakeUpperCase`, `MakeLowerCase`, `Capitalize`,
  `SpeechMenu`, `StartSpeaking`, `StopSpeaking`.

## Format

- Panels and basics: `ShowFonts`, `ShowColors`, `FontMenu`, `Bold`, `Italic`, `Underline`, `Bigger`,
  `Smaller`.
- Kerning: `KerningMenu`, `KerningStandard`, `KerningNone`, `KerningTighten`, `KerningLoosen`.
- Ligatures: `LigaturesMenu`, `LigaturesNone`, `LigaturesStandard`, `LigaturesAll`.
- Baseline: `BaselineMenu`, `BaselineStandard`, `RaiseBaseline`, `LowerBaseline`, `Superscript`,
  `Subscript`.
- Alignment: `TextMenu`, `AlignLeft`, `AlignCenter`, `AlignRight`, `AlignJustified`.

## View, Window, and Help

- View: `ShowToolbar`, `CustomizeToolbar`, `ToggleFullScreen`, `ToggleSidebar`, `ToggleTabBar`.
- Window: `Close`, `Minimize`, `MinimizeAll`, `Zoom`, `BringAllToFront`, `ShowNextTab`,
  `ShowPreviousTab`, `MergeAllWindows`, `MoveTabToNewWindow`.
- Help: `AppHelp`.

For Show/Hide functions, `state` represents current visibility and determines the default title and
checkmark. Update that state from the callback to keep the next composition in sync.

## Custom building blocks

`Item` adds an action, `Checkbox` adds controlled Boolean state, `Menu` nests children, `Section`
adds a header surrounded by separators, and `Separator` divides groups. Use Kotlin `if`, `when`, and
loops directly; there is no separate visibility property.

See [Icons, shortcuts, and localization](icons-shortcuts-localization.md) for `MenuIcon`,
`MenuShortcut`, vector conversion, and translated standard titles.
