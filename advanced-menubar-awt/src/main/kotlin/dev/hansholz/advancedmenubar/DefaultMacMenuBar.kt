@file:OptIn(InternalAdvancedMenuBarApi::class)

package dev.hansholz.advancedmenubar

import TipsIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import dev.hansholz.advancedmenubar.MenuIcon.SFSymbol
import org.jetbrains.skiko.hostOs
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

/**
 * Installs a conventional native macOS application menu with common Edit, View, Window, and Help
 * entries for this Compose Desktop window.
 *
 * Standard entries without callbacks use AppKit or the Compose edit-command bridge. Optional menus
 * can be removed with their corresponding flags.
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
fun FrameWindowScope.DefaultMacMenuBar(
    appName: String = window.title,
    onAboutClick: (() -> Unit)? = null,
    onSettingsClick: (() -> Unit)? = null,
    onHelpClick: (() -> Unit)? = null,
    editMenu: Boolean = true,
    viewMenu: Boolean = true,
    windowMenu: Boolean = true,
    helpMenu: Boolean = true,
) {
    if (!hostOs.isMacOS) return

    var isFullscreen by remember(window) {
        mutableStateOf(window.placement == WindowPlacement.Fullscreen)
    }
    DisposableEffect(window) {
        val listener =
            object : ComponentAdapter() {
                override fun componentResized(event: ComponentEvent?) {
                    isFullscreen = window.placement == WindowPlacement.Fullscreen
                }
            }
        window.addComponentListener(listener)
        onDispose { window.removeComponentListener(listener) }
    }

    val majorVersion =
        remember {
            System.getProperty("os.version").substringBefore('.').toIntOrNull() ?: 0
        }

    fun symbol(name: String): MenuIcon? = if (majorVersion >= 26) SFSymbol(name) else null
    val tipsIcon = rememberMenuIconFrom(TipsIcon)
    val editCommandsEnabled = defaultEditMenuEnabled()

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
                Undo(enabled = editCommandsEnabled, icon = symbol("arrow.uturn.backward"))
                Redo(enabled = editCommandsEnabled, icon = symbol("arrow.uturn.forward"))
                Separator()
                Cut(enabled = editCommandsEnabled, icon = symbol("scissors"))
                Copy(enabled = editCommandsEnabled, icon = symbol("doc.on.doc"))
                Paste(enabled = editCommandsEnabled, icon = symbol("doc.on.clipboard"))
                Delete(enabled = editCommandsEnabled, icon = symbol("delete.left"))
                SelectAll(enabled = editCommandsEnabled, icon = symbol("character.textbox"))
            }
        }

        if (viewMenu) {
            ViewMenu {
                ToggleFullScreen(
                    state = isFullscreen,
                    icon =
                        symbol(
                            if (isFullscreen) {
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
