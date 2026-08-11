package dev.hansholz.advancedmenubar

import TipsIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.hansholz.advancedmenubar.MenuIcon.SFSymbol
import dev.nucleusframework.window.tao.TaoDecoratedWindowScope
import org.jetbrains.skiko.hostOs

/**
 * Installs the conventional native macOS application, Edit, View, Window, and Help menus for a
 * Nucleus Tao decorated window.
 *
 * Fullscreen and active-window state are read directly from the Tao scope; AWT is not used.
 *
 * @param appName name used in application-specific localized labels.
 * @param onAboutClick custom About action, or `null` for AppKit's standard About panel.
 * @param onSettingsClick optional Settings action; the item is omitted when `null`.
 * @param onHelpClick optional application Help action.
 * @param editMenu whether to include the standard Edit menu.
 * @param viewMenu whether to include the fullscreen View menu.
 * @param windowMenu whether to include the standard Window menu.
 * @param helpMenu whether to include the Help menu.
 */
@Composable
fun TaoDecoratedWindowScope.DefaultMacMenuBar(
    appName: String,
    onAboutClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onHelpClick: (() -> Unit)? = null,
    editMenu: Boolean = true,
    viewMenu: Boolean = true,
    windowMenu: Boolean = true,
    helpMenu: Boolean = true,
) {
    if (!hostOs.isMacOS) return

    val majorVersion =
        remember {
            System.getProperty("os.version").substringBefore('.').toIntOrNull() ?: 0
        }

    fun symbol(name: String): MenuIcon? = if (majorVersion >= 26) SFSymbol(name) else null
    val tipsIcon = rememberMenuIconFrom(TipsIcon)

    AdvancedMacMenuBar(appName) {
        MacApplicationMenu {
            About(onClick = onAboutClick, icon = symbol("info.circle"))
            Separator()
            onSettingsClick?.let {
                Settings(onClick = it, icon = symbol("gear"))
                Separator()
            }
            Services()
            Separator()
            Hide()
            HideOthers()
            ShowAll()
            Separator()
            Quit()
        }

        if (editMenu) {
            EditMenu {
                Undo(icon = symbol("arrow.uturn.backward"))
                Redo(icon = symbol("arrow.uturn.forward"))
                Separator()
                Cut(icon = symbol("scissors"))
                Copy(icon = symbol("doc.on.doc"))
                Paste(icon = symbol("doc.on.clipboard"))
                Delete(icon = symbol("delete.left"))
                SelectAll(icon = symbol("character.textbox"))
            }
        }

        if (viewMenu) {
            ViewMenu {
                ToggleFullScreen(
                    state = state.isFullscreen,
                    icon =
                        symbol(
                            if (state.isFullscreen) {
                                "arrow.down.right.and.arrow.up.left.rectangle"
                            } else {
                                "arrow.up.left.and.arrow.down.right.rectangle"
                            },
                        ),
                )
            }
        }

        if (windowMenu) {
            WindowMenu {
                Minimize()
                Zoom()
                Separator()
                BringAllToFront()
            }
        }

        if (helpMenu) {
            HelpMenu {
                onHelpClick?.let { AppHelp(onClick = it, icon = tipsIcon) }
            }
        }
    }
}
