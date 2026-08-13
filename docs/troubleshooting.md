# Troubleshooting and limitations

## No native menu appears

- Confirm the AWT/Tao backend matches the window scope.
- Run the app on macOS; `DefaultMacMenuBar` and `AdvancedMacMenuBar` are intentionally macOS-only.
- Keep the selected artifact and `advanced-menubar/native/**` resources in the packaged runtime.
- With aggressive shrinking, include the artifact's bundled ProGuard configuration and JNI metadata.
- Set `ADVANCED_MENUBAR_DEBUG=1` before launch to print native menu action diagnostics.

Native macOS menu functions never switch to Swing after a load failure. `CompatibilityMenuBar`
chooses its renderer by operating system. The text context-menu provider is different: it preserves
Compose's popup if native loading fails, so text editing retains a usable fallback.

## An item is disabled in Swing

AppKit can execute callback-free selectors through its responder chain. Swing cannot, so supply a
callback for each standard action rendered on Windows/Linux. Services and the macOS application
menu have no Swing equivalent.

## Default Edit items do not follow text focus

Place `DefaultMacMenuBar` inside the `NativeTextContextMenuProvider` content scope. Outside that
scope its Edit items intentionally remain enabled, while custom `AdvancedMacMenuBar` and
`CompatibilityMenuBar` declarations keep their explicitly configured state.

Tracking follows Compose platform text-input sessions. Read-only and disabled fields therefore do
not activate the items. A custom editor that handles keys without Compose platform text input cannot
be detected. A field configured not to start input on programmatic focus is detected once it starts
an input session, for example after direct interaction.

## An icon or property is missing

SF Symbols are native macOS assets. Swing supports PNG/file icons only and intentionally omits
subtitles and badges. On macOS, individual SF Symbols, subtitles, badges, and system Edit services
remain subject to operating-system availability.

## Theme or multiple windows look wrong

`NativeTextContextMenuProvider(isDark = …)` sets the entire `NSApplication` appearance. Use one
application-level theme value across windows, or pass `null` and manage AppKit appearance elsewhere.
Only the focused window supplies the native main menu; ensure each window's menu remains composed.

## Runtime locale changes do not update labels

After changing `Locale.getDefault()`, rebuild/key the menu subtree with the selected locale so
Compose resources are resolved again.

## Behavior differs between development and packaged runs

Small differences between AWT and Tao, and between Gradle `run`, distributable, release, and GraalVM
builds, are expected around macOS system integrations. They use different window/event-loop and
packaging environments even though the Advanced Menubar DSL is the same.

For example, starting Dictation from the native Edit menu may not hand text back correctly when the
Tao sample is launched through its development `run` task. Use the **Tao Sample Distributable** or
**Tao Sample GraalVM** run configuration when validating Dictation and other system input services.
Always verify release-critical native behavior in the same backend and packaging format that will be
shipped.
