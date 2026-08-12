# ProGuard

Compose Desktop currently does not load ProGuard configuration bundled with libraries. When
ProGuard is enabled for release builds, add these rules manually to your application's ProGuard
file (for example, `src/main/compose-desktop.pro`):

```proguard
-keep class dev.hansholz.advancedmenubar.NativeMenuBridge { *; }
-keep class dev.hansholz.advancedmenubar.NativeTextContextMenuBridge { *; }
```

Make sure the file is included in the Compose Desktop configuration:

```kotlin
compose.desktop {
    application {
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/main/compose-desktop.pro"))
        }
    }
}
```

Without these rules, ProGuard may rename or remove the JNI callback classes, causing native menu
functionality to fail in the packaged application.

## GraalVM Native Image

Advanced Menubar artifacts include the JNI reachability metadata, Compose resources, and native
macOS libraries required for GraalVM Native Image. No library-specific reflection or resource
configuration is needed. Native macOS executables must be built on macOS.

The Tao sample demonstrates the Nucleus/GraalVM setup.
