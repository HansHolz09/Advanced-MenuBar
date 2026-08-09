package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import dev.nucleusframework.window.tao.TaoDecoratedWindowScope
import org.jetbrains.skiko.hostOs

/**
 * Installs a native macOS menu for a Nucleus Tao decorated window.
 *
 * Menu ownership follows the window's active state and does not initialize or fall back to AWT.
 */
@Composable
fun TaoDecoratedWindowScope.AdvancedMacMenuBar(
    appName: String,
    content: MenuBarScope.() -> Unit,
) {
    if (!hostOs.isMacOS) return

    NativeMacMenuBar(
        appName = appName,
        active = state.isActive,
        content = content,
    )
}
