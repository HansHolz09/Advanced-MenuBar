# GraalVM Native Image

Published artifacts include JNI reachability metadata, localized Compose resources, and native
macOS libraries for Apple Silicon and Intel. Applications normally need no Advanced Menubar-specific
reflection or resource configuration.

```kotlin
dependencies {
    implementation("dev.hansholz:advanced-menubar-tao:current-version")
}
```

Keep the selected backend on the runtime classpath and initialize the native-image runtime required
by your application framework. The Tao sample demonstrates the Nucleus/GraalVM combination. Build a
macOS executable on macOS; Advanced Menubar's native bridge links Cocoa and selects the packaged
architecture at runtime.

Application-owned reflection, callbacks, and resources still belong in the application's normal
reachability configuration.
