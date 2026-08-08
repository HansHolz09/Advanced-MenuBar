import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.selectAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.asAwtTransferable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import composeadvancedmenubar.sample.awt.generated.resources.Res
import composeadvancedmenubar.sample.awt.generated.resources.allStringResources
import composeadvancedmenubar.sample.awt.generated.resources.available_version
import composeadvancedmenubar.sample.awt.generated.resources.check_for_updates
import composeadvancedmenubar.sample.awt.generated.resources.checkbox_item
import composeadvancedmenubar.sample.awt.generated.resources.community_forum
import composeadvancedmenubar.sample.awt.generated.resources.custom
import composeadvancedmenubar.sample.awt.generated.resources.custom_item
import composeadvancedmenubar.sample.awt.generated.resources.custom_submenu
import composeadvancedmenubar.sample.awt.generated.resources.disabled_item
import composeadvancedmenubar.sample.awt.generated.resources.file
import composeadvancedmenubar.sample.awt.generated.resources.new_update_available
import composeadvancedmenubar.sample.awt.generated.resources.options
import composeadvancedmenubar.sample.awt.generated.resources.release_notes
import composeadvancedmenubar.sample.awt.generated.resources.resources
import composeadvancedmenubar.sample.awt.generated.resources.section
import composeadvancedmenubar.sample.awt.generated.resources.website
import dev.hansholz.advancedmenubar.CompatibilityMenuBar
import dev.hansholz.advancedmenubar.MenuIcon.SFSymbol
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.skiko.hostOs
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FrameWindowScope.MenuBar(
    customMenus: List<Int>,
    checkboxes: List<MutableState<Boolean>>,
    textFieldState: TextFieldState,
    onClick: (String) -> Unit,
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    val strings =
        Res.allStringResources.map {
            it.value to stringResource(it.value)
        }

    fun getString(stringResource: StringResource): String = strings.find { it.first == stringResource }?.second ?: "STRING NOT FOUND"

    var isFullscreen by remember(window) { mutableStateOf(window.placement == WindowPlacement.Fullscreen) }
    DisposableEffect(window) {
        val listener =
            object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent?) {
                    isFullscreen = window.placement == WindowPlacement.Fullscreen
                }
            }
        window.addComponentListener(listener)
        onDispose { window.removeComponentListener(listener) }
    }

    val canUndo by remember { derivedStateOf { textFieldState.undoState.canUndo } }
    val canRedo by remember { derivedStateOf { textFieldState.undoState.canRedo } }
    val selectionCollapsed by remember { derivedStateOf { textFieldState.selection.collapsed } }
    val textIsNotEmpty by remember { derivedStateOf { textFieldState.text.isNotEmpty() } }

    CompatibilityMenuBar {
        MacApplicationMenu {
            About(icon = SFSymbol("info.circle")) { onClick("About") }
            Separator()
            Item(
                title = getString(Res.string.check_for_updates),
                icon = SFSymbol("arrow.down.app"),
                subtitle = "${getString(Res.string.available_version)}: 1.0.0",
                tooltip = "Changelog:\n- First big new Feature\n- Fixed a big security issue\n- Many little bugfixes and stability improvements",
                badge = getString(Res.string.new_update_available),
            ) {
                onClick("Check for Updates")
            }
            Separator()
            Settings(icon = SFSymbol("gear")) { onClick("Settings") }
            Separator()
            Services()
            Separator()
            Hide()
            HideOthers()
            ShowAll()
            Separator()
            Quit()
        }
        FileMenu {
            FileNew { onClick("New File") }
            FileOpen { onClick("Open File") }
            FileOpenRecent {
                Item("PDF_01", icon = SFSymbol("doc.richtext")) { onClick("PDF_01") }
                Item("Picture_02", icon = SFSymbol("photo")) { onClick("Picture_02") }
                Separator()
                FileClearRecent { onClick("Clear Recent Files") }
            }
            Separator()
            FileSave { onClick("Save File") }
            FileSaveAs { onClick("Save File as") }
            FileRename { onClick("Rename File") }
            Separator()
            if (hostOs.isMacOS) FilePageSetup()
            FilePrint { onClick("Print") }
        }
        EditMenu {
            Undo(enabled = canUndo) {
                textFieldState.undoState.undo()
            }
            Redo(enabled = canRedo) {
                textFieldState.undoState.redo()
            }
            Separator()
            Cut(enabled = !selectionCollapsed) {
                val sel = textFieldState.selection
                if (!sel.collapsed) {
                    val text = textFieldState.text.substring(sel.start, sel.end)
                    scope.launch {
                        clipboard.setPlainText(text)
                        textFieldState.edit { delete(sel.start, sel.end) }
                    }
                }
            }
            Copy(enabled = !selectionCollapsed) {
                val sel = textFieldState.selection
                if (!sel.collapsed) {
                    val text = textFieldState.text.substring(sel.start, sel.end)
                    scope.launch { clipboard.setPlainText(text) }
                }
            }
            Paste(enabled = clipboard.hasPlainText()) {
                scope.launch {
                    val paste = clipboard.readPlainText()
                    val sel = textFieldState.selection
                    textFieldState.edit {
                        if (!sel.collapsed) delete(sel.start, sel.end)
                        insert(selection.start, paste)
                        placeCursorBeforeCharAt(sel.start + paste.length)
                    }
                }
            }
            PasteAndMatchStyle(enabled = false) {}
            Delete(enabled = !selectionCollapsed) {
                val sel = textFieldState.selection
                if (!sel.collapsed) textFieldState.edit { delete(sel.start, sel.end) }
            }
            SelectAll(enabled = textIsNotEmpty) {
                textFieldState.edit { selectAll() }
            }
        }
        if (hostOs.isMacOS) {
            ViewMenu {
                ShowToolbar(false, enabled = false) {}
                CustomizeToolbar(enabled = false) {}
                Separator()
                ToggleFullScreen(isFullscreen)
            }
        }
        customMenus.forEach {
            CustomMenu("${getString(Res.string.custom)} $it") {
                Section("${getString(Res.string.section)} 1") {
                    Item("${getString(Res.string.custom_item)} 1") { onClick("Custom Item 1 (from Custom $it)") }
                    Item("${getString(Res.string.custom_item)} 2") { onClick("Custom Item 2 (from Custom $it)") }
                }
                Section("${getString(Res.string.section)} 2") {
                    Menu(getString(Res.string.custom_submenu)) {
                        Menu("${getString(Res.string.file)} 2") {
                            Item(getString(Res.string.disabled_item), enabled = false) {}
                        }
                    }
                    Item(getString(Res.string.disabled_item), enabled = false) {}
                }
            }
        }
        CustomMenu(getString(Res.string.options)) {
            checkboxes.forEachIndexed { index, value ->
                Checkbox("${getString(Res.string.checkbox_item)} ${index + 1}", value.value) { value.value = it }
            }
        }
        if (hostOs.isMacOS) {
            WindowMenu {
                Close()
                Minimize()
                MinimizeAll()
                Zoom()
                Separator()
                BringAllToFront()
            }
        }
        HelpMenu {
            AppHelp { onClick("Help") }
            Separator()
            Item(getString(Res.string.release_notes)) { onClick("Release Notes") }
            Menu(getString(Res.string.resources)) {
                Item(getString(Res.string.website)) { onClick("Website") }
                Item(getString(Res.string.community_forum)) { onClick("Community Forum") }
            }
        }
    }
}

private fun Clipboard.hasPlainText(): Boolean =
    (nativeClipboard as? java.awt.datatransfer.Clipboard)
        ?.isDataFlavorAvailable(DataFlavor.stringFlavor) == true

@OptIn(ExperimentalComposeUiApi::class)
private suspend fun Clipboard.setPlainText(text: String) {
    setClipEntry(ClipEntry(StringSelection(text)))
}

@OptIn(ExperimentalComposeUiApi::class)
private suspend fun Clipboard.readPlainText(): String =
    runCatching {
        getClipEntry()
            ?.asAwtTransferable
            ?.getTransferData(DataFlavor.stringFlavor) as? String
    }.getOrNull().orEmpty()
