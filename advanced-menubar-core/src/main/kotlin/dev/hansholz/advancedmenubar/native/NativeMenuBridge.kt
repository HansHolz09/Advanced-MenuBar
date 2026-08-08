package dev.hansholz.advancedmenubar

import dev.nucleusframework.core.runtime.NativeLibraryLoader
import java.util.concurrent.ConcurrentHashMap

internal object NativeMenuBridge {
    private const val LIBRARY_NAME = "advanced_menubar"
    private val callbacks = ConcurrentHashMap<Long, () -> Unit>()

    val isAvailable: Boolean =
        NativeLibraryLoader.load(
            libraryName = LIBRARY_NAME,
            callerClass = NativeMenuBridge::class.java,
            resourcePrefix = "/advanced-menubar/native",
        )

    fun install(encodedMenu: EncodedMenu): Long {
        if (!isAvailable) return 0
        val handle = nativeInstallMenu(encodedMenu.payload)
        if (handle == 0L) return 0
        updateCallbacks(encodedMenu.callbacks)
        return handle
    }

    fun updateCallbacks(next: Map<Long, () -> Unit>) {
        callbacks.clear()
        callbacks.putAll(next)
    }

    fun retainMainMenu(): Long = if (isAvailable) nativeRetainMainMenu() else 0

    fun restoreMainMenu(handle: Long) {
        if (isAvailable) nativeRestoreMainMenu(handle)
    }

    fun release(handle: Long) {
        if (isAvailable && handle != 0L) nativeRelease(handle)
    }

    fun clearCallbacks() {
        callbacks.clear()
    }

    @JvmStatic
    fun onAction(actionId: Long) {
        callbacks[actionId]?.invoke()
    }

    @JvmStatic private external fun nativeInstallMenu(payload: ByteArray): Long

    @JvmStatic private external fun nativeRetainMainMenu(): Long

    @JvmStatic private external fun nativeRestoreMainMenu(handle: Long)

    @JvmStatic private external fun nativeRelease(handle: Long)
}

internal class EncodedMenu(
    val payload: ByteArray,
    val callbacks: Map<Long, () -> Unit>,
)
