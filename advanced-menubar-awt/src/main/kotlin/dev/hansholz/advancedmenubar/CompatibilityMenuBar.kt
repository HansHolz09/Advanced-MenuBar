package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import org.jetbrains.skiko.hostOs

/**
 * Uses [AdvancedMacMenuBar] on macOS and [SwingMenuBar] on Windows and Linux.
 *
 * The macOS branch remains strictly native. Swing is selected by operating system, not as a
 * recovery path when the native bridge fails to load.
 */
@Composable
fun FrameWindowScope.CompatibilityMenuBar(
    appName: String = window.title,
    content: MenuBarScope.() -> Unit,
) = if (hostOs.isMacOS) {
    AdvancedMacMenuBar(appName, content)
} else {
    SwingMenuBar(appName, content)
}
