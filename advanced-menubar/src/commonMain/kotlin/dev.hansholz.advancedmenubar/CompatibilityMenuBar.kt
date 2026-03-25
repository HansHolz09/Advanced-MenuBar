package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import org.jetbrains.skiko.hostOs

@Composable
fun FrameWindowScope.CompatibilityMenuBar(
    appName: String = window.title,
    content: AdvancedMenuBarScope.() -> Unit
) = if (hostOs.isMacOS) {
        AdvancedMacMenuBar(appName, content)
    } else {
        SwingMenuBar(appName, content)
    }