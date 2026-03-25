package dev.hansholz.advancedmenubar

import org.jetbrains.skiko.hostOs

enum class MenuVisibility {
    MACOS_ONLY,
    WINDOWS_AND_LINUX_ONLY,
    ALWAYS_VISIBLE,
}

internal fun ifVisible(visibility: MenuVisibility, block: () -> Unit) {
    if (
        when (visibility) {
            MenuVisibility.MACOS_ONLY -> hostOs.isMacOS
            MenuVisibility.WINDOWS_AND_LINUX_ONLY -> !hostOs.isMacOS
            MenuVisibility.ALWAYS_VISIBLE -> true
        }
    ) {
        block()
    }
}