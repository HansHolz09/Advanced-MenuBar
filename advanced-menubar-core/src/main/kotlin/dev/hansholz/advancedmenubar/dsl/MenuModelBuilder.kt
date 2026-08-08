package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import composeadvancedmenubar.generated.resources.Res
import composeadvancedmenubar.generated.resources.allStringResources
import org.jetbrains.compose.resources.stringResource

@Composable
fun buildMenuModel(
    appName: String,
    content: MenuBarScope.() -> Unit,
): List<MenuModel.TopMenu> {
    val strings =
        Res.allStringResources.map {
            it.value to stringResource(it.value, appName)
        }
    val scope =
        MenuBarScope(strings).apply {
            reset()
            content()
        }
    return scope.menus.toList()
}
