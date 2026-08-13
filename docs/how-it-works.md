# How it works

The DSL creates a backend-neutral internal menu description from localized Compose resources and
current state. The macOS renderer encodes it into a compact binary payload; a project-owned JNI
bridge creates `NSMenu`, `NSMenuItem`, targets, selectors, images, and system-menu registrations.

Callbacks are held on the JVM side. If recomposition changes only callback captures, the native menu
stays installed and callback references are replaced. A structural or visual payload change installs
a new menu. A focus-aware coordinator ensures that the active AWT or Tao window owns the process-wide
main menu and restores the previous menu when the final owner leaves composition.

Standard application, Services, Window, and Help actions remain AppKit behavior. Compose Edit
commands synthesize the corresponding key event for the focused window. AppKit supplies modern
automatic Edit entries where available. The Swing renderer reads the same description but applies
Windows/Linux accelerator conventions and only exposes capabilities supported by ordinary Swing
menus.

Native text popups use an editable or read-only `NSTextView` to match the Compose text owner.
AppKit transformations are forwarded directly to Compose's current text-state editing path. The
Compose paste action and a temporary pasteboard value are retained only as a compatibility fallback
for unknown Compose text-manager implementations.

The text context-menu provider also intercepts Compose's common platform text-input sessions and
exposes whether one is active to a `DefaultMacMenuBar` in its scope. This keeps AWT and Tao on the
same path and covers state-based, legacy, and secure Compose text fields. The original input request
is forwarded unchanged, so IME handling, transformations, context-menu actions, and window menu
ownership continue through their existing implementations.

The native bridge uses some availability-checked AppKit behavior for system-generated items. Their
exact presence and appearance may vary by macOS release.
