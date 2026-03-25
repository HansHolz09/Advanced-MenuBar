package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.window.FrameWindowScope
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.TopMenu
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import javax.swing.SwingUtilities

@Composable
fun FrameWindowScope.AdvancedMacMenuBar(
    appName: String = window.title,
    content: AdvancedMenuBarScope.() -> Unit
) {
    val model = MenuScopeToList(appName, content)
    AdvancedMacMenuBarImplementation(model)
}

@Composable
internal fun FrameWindowScope.AdvancedMacMenuBarImplementation(model: List<TopMenu>) {
    DisposableEffect(window) {
        val listener = object : WindowFocusListener {
            override fun windowGainedFocus(e: WindowEvent?) {
                SwingUtilities.invokeLater {
                    MacCocoaMenuBar.rebuildMenuBar(model)
                }
            }
            override fun windowLostFocus(e: WindowEvent?) {}
        }
        window.addWindowFocusListener(listener)
        onDispose { window.removeWindowFocusListener(listener) }
    }

    val lastUpdate = remember { object { var time = 0L } }
    LaunchedEffect(model) {
        val now = System.currentTimeMillis()
        if (now - lastUpdate.time > 250) {
            lastUpdate.time = now
            SwingUtilities.invokeLater {
                MacCocoaMenuBar.rebuildMenuBar(model)
            }
        }
    }
}