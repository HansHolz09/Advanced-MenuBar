package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.FrameWindowScope
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener

/**
 * Installs a native macOS menu for this Compose Desktop window.
 *
 * Focus changes automatically transfer ownership of the process-wide AppKit main menu between
 * windows. The function does not install a Swing fallback on other platforms or after a native
 * bridge loading failure; use [CompatibilityMenuBar] when cross-platform rendering is wanted.
 *
 * @param appName name used by the application menu and localized labels.
 * @param content declarative menu structure.
 */
@Composable
fun FrameWindowScope.AdvancedMacMenuBar(
    appName: String = window.title,
    content: MenuBarScope.() -> Unit,
) {
    var active by remember(window) { mutableStateOf(window.isFocused) }
    DisposableEffect(window) {
        val listener =
            object : WindowFocusListener {
                override fun windowGainedFocus(event: WindowEvent?) {
                    active = true
                }

                override fun windowLostFocus(event: WindowEvent?) {
                    active = false
                }
            }
        window.addWindowFocusListener(listener)
        active = window.isFocused
        onDispose { window.removeWindowFocusListener(listener) }
    }

    NativeMacMenuBar(appName = appName, active = active, content = content)
}
