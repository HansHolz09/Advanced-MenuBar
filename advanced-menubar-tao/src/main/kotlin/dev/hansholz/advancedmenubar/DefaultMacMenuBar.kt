@file:OptIn(InternalAdvancedMenuBarApi::class)

package dev.hansholz.advancedmenubar

import TipsIcon
import androidx.compose.runtime.Composable
import dev.hansholz.advancedmenubar.MenuIcon.SFSymbol
import dev.hansholz.advancedmenubar.utils.majorSystemVersion
import dev.nucleusframework.window.tao.TaoDecoratedWindowScope
import org.jetbrains.skiko.hostOs

/**
 * Installs the conventional native macOS application, Edit, View, Window, and Help menus for a
 * Nucleus Tao decorated window.
 *
 * Fullscreen and active-window state are read directly from the Tao scope; AWT is not used.
 * Inside [NativeTextContextMenuProvider], Edit entries follow Compose's active text-input session;
 * outside it they remain enabled.
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

    fun symbol(name: String): MenuIcon? = if (majorSystemVersion >= 26) SFSymbol(name) else null

    fun symbol26(name: String): MenuIcon? = if (majorSystemVersion == 26) SFSymbol(name) else null

    val tipsIcon = if (majorSystemVersion >= 26) rememberMenuIconFrom(TipsIcon) else null

    val editCommandsEnabled = defaultEditMenuEnabled()

    AdvancedMacMenuBar(appName) {
        MacApplicationMenu {
            About(onClick = onAboutClick, icon = symbol26("info.circle"))
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
                Undo(enabled = editCommandsEnabled, icon = symbol26("arrow.uturn.backward"))
                Redo(enabled = editCommandsEnabled, icon = symbol26("arrow.uturn.forward"))
                Separator()
                Cut(enabled = editCommandsEnabled, icon = symbol26("scissors"))
                Copy(enabled = editCommandsEnabled, icon = symbol26("doc.on.doc"))
                Paste(enabled = editCommandsEnabled, icon = symbol26("doc.on.clipboard"))
                Delete(enabled = editCommandsEnabled, icon = symbol26("delete.left"))
                SelectAll(enabled = editCommandsEnabled, icon = symbol26("character.textbox"))
            }
        }

        if (viewMenu) {
            ViewMenu {
                ToggleFullScreen(
                    state = state.isFullscreen,
                    icon =
                        symbol26(
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
