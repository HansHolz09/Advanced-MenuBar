package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import composeadvancedmenubar.advanced_menubar.generated.resources.Res
import composeadvancedmenubar.advanced_menubar.generated.resources.allStringResources
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MenuScopeToList(appName: String, content: AdvancedMenuBarScope.() -> Unit): List<MacCocoaMenuBar.TopMenu> {
    val strings = Res.allStringResources.map {
        it.value to stringResource(it.value, appName)
    }
    val scope = AdvancedMenuBarScope(strings).apply {
        reset()
        content()
    }
    return scope.menus.toList()
}