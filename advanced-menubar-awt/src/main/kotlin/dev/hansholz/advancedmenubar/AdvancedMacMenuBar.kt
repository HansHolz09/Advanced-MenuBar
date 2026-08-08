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
