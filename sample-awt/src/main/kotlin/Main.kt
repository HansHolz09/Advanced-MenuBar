import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.hansholz.advancedmenubar.ContextMenuAction
import dev.hansholz.advancedmenubar.DefaultMacMenuBar
import dev.hansholz.advancedmenubar.MenuBarLanguage
import dev.hansholz.advancedmenubar.NativeTextContextMenuProvider
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
fun main() =
    application {
        val language = remember { mutableStateOf<MenuBarLanguage?>(null) }
        val isDark = remember { mutableStateOf(false) }

        val windows = remember { mutableStateListOf("Advanced MenuBar" to true) }
        windows.forEachIndexed { index, (title, visible) ->
            if (visible) {
                Window(
                    onCloseRequest = {
                        if (windows.size == 1) {
                            exitApplication()
                        } else {
                            windows[index] = title to false
                        }
                    },
                    title = title,
                ) {
                    window.apply {
                        rootPane.putClientProperty("apple.awt.fullWindowContent", true)
                        rootPane.putClientProperty("apple.awt.transparentTitleBar", true)
                        rootPane.putClientProperty("apple.awt.windowTitleVisible", true)
                    }

                    val clickedItems = remember { mutableStateListOf<String>() }
                    val customMenus = remember { mutableStateListOf(1) }

                    val selectedMenu = remember { mutableStateOf(0) }

                    val checkboxes = List(3) { remember { mutableStateOf(it != 0) } }
                    val textFieldState = rememberTextFieldState()

                    key(language.value) {
                        when (selectedMenu.value) {
                            0 ->
                                MenuBar(
                                    customMenus = customMenus,
                                    checkboxes = checkboxes,
                                    textFieldState = textFieldState,
                                ) {
                                    clickedItems += it
                                }
                            1 ->
                                DefaultMacMenuBar(
                                    onAboutClick = { clickedItems += "About" },
                                    onSettingsClick = { clickedItems += "Settings" },
                                    onHelpClick = { clickedItems += "Help" },
                                )
                            2 -> FullMacMenuBar("Advanced MenuBar")
                        }
                    }

                    NativeTextContextMenuProvider(
                        isDark = isDark.value,
                        customActions =
                            listOf(
                                ContextMenuAction(
                                    label = "Custom Context Item",
                                    systemImageName = "contextualmenu.and.pointer.arrow",
                                ) { clickedItems += "Custom Context Item" },
                                ContextMenuAction(
                                    label = "Second Custom Context Item",
                                ) { clickedItems += "Second Custom Context Item" },
                            ),
                    ) {
                        App(
                            language = language,
                            isDark = isDark,
                            clickedItems = clickedItems,
                            customMenus = customMenus,
                            selectedMenu = selectedMenu,
                            checkboxes = checkboxes,
                            textFieldState = textFieldState,
                        ) {
                            windows += "Advanced MenuBar ${windows.size + 1}" to true
                        }
                    }
                }
            }
        }
    }
