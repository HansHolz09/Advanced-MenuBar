package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.TopMenu
import javax.swing.SwingUtilities

@Composable
fun AdvancedMacMenuBar(appName: String, content: AdvancedMenuBarScope.() -> Unit) {
    val model = MenuScopeToList(appName, content)
    AdvancedMacMenuBarImplementation(model)
}

@Composable
internal fun AdvancedMacMenuBarImplementation(model: List<TopMenu>) {
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