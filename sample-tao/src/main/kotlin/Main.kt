import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import dev.hansholz.advancedmenubar.ContextMenuAction
import dev.hansholz.advancedmenubar.DefaultMacMenuBar
import dev.hansholz.advancedmenubar.MenuBarLanguage
import dev.hansholz.advancedmenubar.NativeTextContextMenuProvider
import dev.nucleusframework.graalvm.GraalVmInitializer
import dev.nucleusframework.window.TitleBarPlacement
import dev.nucleusframework.window.WindowScaffold
import dev.nucleusframework.window.tao.DecoratedWindow
import dev.nucleusframework.window.tao.taoApplication
import dev.nucleusframework.window.windowDragArea

fun main() {
    GraalVmInitializer.initialize()
    taoApplication {
        val language = remember { mutableStateOf<MenuBarLanguage?>(null) }
        val isDark = remember { mutableStateOf(false) }
        val windows = remember { mutableStateListOf(1) }

        windows.toList().forEach { id ->
            key(id) {
                val title = if (id == 1) "Advanced Menubar" else "Advanced Menubar $id"
                DecoratedWindow(
                    onCloseRequest = {
                        windows.remove(id)
                        if (windows.isEmpty()) exitApplication()
                    },
                    state = rememberWindowState(),
                    title = title,
                ) {
                    val clickedItems = remember { mutableStateListOf<String>() }
                    val customMenus = remember { mutableStateListOf(1) }
                    val selectedMenu = remember { mutableStateOf(0) }
                    val checkboxes = List(3) { remember { mutableStateOf(it != 0) } }
                    val textFieldState = rememberTextFieldState()

                    key(language.value) {
                        when (selectedMenu.value) {
                            0 ->
                                MenuBar(
                                    appName = "Advanced Menubar",
                                    customMenus = customMenus,
                                    checkboxes = checkboxes,
                                    textFieldState = textFieldState,
                                ) { clickedItems += it }
                            1 ->
                                DefaultMacMenuBar(
                                    appName = "Advanced Menubar",
                                    onAboutClick = { clickedItems += "About" },
                                    onSettingsClick = { clickedItems += "Settings" },
                                    onHelpClick = { clickedItems += "Help" },
                                )
                            else -> FullMacMenuBar("Advanced Menubar")
                        }
                    }

                    WindowScaffold(
                        titleBar = { Box(Modifier.fillMaxWidth().height(32.dp).windowDragArea()) },
                        titleBarPlacement =
                            TitleBarPlacement.Overlay(
                                autoHideInFullscreen = false,
                                passThroughToContent = true,
                            ),
                    ) {
                        NativeTextContextMenuProvider(
                            isDark = isDark.value,
                            customActions =
                                listOf(
                                    ContextMenuAction("Custom Context Item", "contextualmenu.and.pointer.arrow") {
                                        clickedItems += "Custom Context Item"
                                    },
                                    ContextMenuAction("Second Custom Context Item") {
                                        clickedItems += "Second Custom Context Item"
                                    },
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
                                onNewWindow = { windows += (windows.maxOrNull() ?: 0) + 1 },
                            )
                        }
                    }
                }
            }
        }
    }
}
