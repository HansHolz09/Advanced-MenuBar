import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.selectAll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.asAwtTransferable
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Info
import composeadvancedmenubar.sample.tao.generated.resources.Res
import composeadvancedmenubar.sample.tao.generated.resources.allStringResources
import composeadvancedmenubar.sample.tao.generated.resources.available_version
import composeadvancedmenubar.sample.tao.generated.resources.check_for_updates
import composeadvancedmenubar.sample.tao.generated.resources.checkbox_item
import composeadvancedmenubar.sample.tao.generated.resources.community_forum
import composeadvancedmenubar.sample.tao.generated.resources.custom
import composeadvancedmenubar.sample.tao.generated.resources.custom_item
import composeadvancedmenubar.sample.tao.generated.resources.custom_submenu
import composeadvancedmenubar.sample.tao.generated.resources.disabled_item
import composeadvancedmenubar.sample.tao.generated.resources.file
import composeadvancedmenubar.sample.tao.generated.resources.new_update_available
import composeadvancedmenubar.sample.tao.generated.resources.options
import composeadvancedmenubar.sample.tao.generated.resources.release_notes
import composeadvancedmenubar.sample.tao.generated.resources.resources
import composeadvancedmenubar.sample.tao.generated.resources.section
import composeadvancedmenubar.sample.tao.generated.resources.website
import dev.hansholz.advancedmenubar.AdvancedMacMenuBar
import dev.hansholz.advancedmenubar.MenuIcon.SFSymbol
import dev.hansholz.advancedmenubar.rememberMenuIconFrom
import dev.nucleusframework.window.tao.TaoDecoratedWindowScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaoDecoratedWindowScope.MenuBar(
    appName: String,
    customMenus: List<Int>,
    checkboxes: List<MutableState<Boolean>>,
    textFieldState: TextFieldState,
    onClick: (String) -> Unit,
) {
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val strings = Res.allStringResources.map { it.value to stringResource(it.value) }

    fun text(resource: StringResource): String = strings.firstOrNull { it.first == resource }?.second ?: "STRING NOT FOUND"

    val canUndo by remember { derivedStateOf { textFieldState.undoState.canUndo } }
    val canRedo by remember { derivedStateOf { textFieldState.undoState.canRedo } }
    val selectionCollapsed by remember { derivedStateOf { textFieldState.selection.collapsed } }
    val hasText by remember { derivedStateOf { textFieldState.text.isNotEmpty() } }

    val info = rememberMenuIconFrom(MaterialSymbols.Rounded.Info)

    AdvancedMacMenuBar(appName) {
        MacApplicationMenu {
            About(icon = info) { onClick("About") }
            Separator()
            Item(
                title = text(Res.string.check_for_updates),
                icon = SFSymbol("arrow.down.app"),
                subtitle = "${text(Res.string.available_version)}: 1.0.0",
                tooltip = "A native menu item with subtitle, tooltip and badge",
                badge = text(Res.string.new_update_available),
            ) { onClick("Check for Updates") }
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
            FilePageSetup()
            FilePrint { onClick("Print") }
        }
        EditMenu {
            Undo(enabled = canUndo) { textFieldState.undoState.undo() }
            Redo(enabled = canRedo) { textFieldState.undoState.redo() }
            Separator()
            Cut(enabled = !selectionCollapsed) {
                val selection = textFieldState.selection
                if (!selection.collapsed) {
                    val text = textFieldState.text.substring(selection.start, selection.end)
                    scope.launch {
                        clipboard.setPlainText(text)
                        textFieldState.edit { delete(selection.start, selection.end) }
                    }
                }
            }
            Copy(enabled = !selectionCollapsed) {
                val selection = textFieldState.selection
                if (!selection.collapsed) {
                    val text = textFieldState.text.substring(selection.start, selection.end)
                    scope.launch { clipboard.setPlainText(text) }
                }
            }
            Paste(enabled = clipboard.hasPlainText()) {
                scope.launch {
                    val value = clipboard.readPlainText()
                    val selection = textFieldState.selection
                    textFieldState.edit {
                        if (!selection.collapsed) delete(selection.start, selection.end)
                        insert(selection.start, value)
                        placeCursorBeforeCharAt(selection.start + value.length)
                    }
                }
            }
            PasteAndMatchStyle(enabled = false) {}
            Delete(enabled = !selectionCollapsed) {
                val selection = textFieldState.selection
                if (!selection.collapsed) textFieldState.edit { delete(selection.start, selection.end) }
            }
            SelectAll(enabled = hasText) { textFieldState.edit { selectAll() } }
        }
        ViewMenu {
            ShowToolbar(false, enabled = false) {}
            CustomizeToolbar(enabled = false) {}
            Separator()
            ToggleFullScreen(state.isFullscreen)
        }
        customMenus.forEach { number ->
            CustomMenu("${text(Res.string.custom)} $number") {
                Section("${text(Res.string.section)} 1") {
                    Item("${text(Res.string.custom_item)} 1") { onClick("Custom Item 1 (from Custom $number)") }
                    Item("${text(Res.string.custom_item)} 2") { onClick("Custom Item 2 (from Custom $number)") }
                }
                Section("${text(Res.string.section)} 2") {
                    Menu(text(Res.string.custom_submenu)) {
                        Menu("${text(Res.string.file)} 2") {
                            Item(text(Res.string.disabled_item), enabled = false) {}
                        }
                    }
                    Item(text(Res.string.disabled_item), enabled = false) {}
                }
            }
        }
        CustomMenu(text(Res.string.options)) {
            checkboxes.forEachIndexed { index, value ->
                Checkbox("${text(Res.string.checkbox_item)} ${index + 1}", value.value) { value.value = it }
            }
        }
        WindowMenu {
            Close()
            Minimize()
            MinimizeAll()
            Zoom()
            Separator()
            BringAllToFront()
        }
        HelpMenu {
            AppHelp { onClick("Help") }
            Separator()
            Item(text(Res.string.release_notes)) { onClick("Release Notes") }
            Menu(text(Res.string.resources)) {
                Item(text(Res.string.website)) { onClick("Website") }
                Item(text(Res.string.community_forum)) { onClick("Community Forum") }
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
