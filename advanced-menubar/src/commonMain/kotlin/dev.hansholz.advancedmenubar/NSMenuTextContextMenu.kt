package dev.hansholz.advancedmenubar

import androidx.compose.foundation.ContextMenuState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.contextMenuOpenDetector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize
import composeadvancedmenubar.advanced_menubar.generated.resources.Res
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_select_all
import dev.hansholz.advancedmenubar.MacNativeTextContextMenu.captureCurrentNsEvent
import dev.hansholz.advancedmenubar.MacNativeTextContextMenu.dispatchAsyncOnMain
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import javax.swing.SwingUtilities
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
object NSMenuTextContextMenu : TextContextMenu {

    private val LocalNativeTextWriteBack =
        staticCompositionLocalOf<((original: String, replacement: String) -> Unit)?> { null }

    internal var showExtraOptions: Boolean = true
    internal var customItems: List<ContextMenuAction> = emptyList()

    @Composable
    override fun Area(
        textManager: TextContextMenu.TextManager,
        state: ContextMenuState,
        content: @Composable () -> Unit,
    ) {
        val density      = LocalDensity.current
        val windowInfo   = LocalWindowInfo.current
        val writeBack    = LocalNativeTextWriteBack.current

        val currentTextManager  by rememberUpdatedState(textManager)
        val currentShowExtraOptions by rememberUpdatedState(showExtraOptions)
        val currentDensity      by rememberUpdatedState(density)
        val currentWindowInfo   by rememberUpdatedState(windowInfo)
        val currentWriteBack    by rememberUpdatedState(writeBack)
        val currentCustomItems  by rememberUpdatedState(customItems)

        var layoutCoords           by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var pendingNsEventAddress  by remember { mutableStateOf(0L) }

        val selectAllText = stringResource(Res.string.edit_select_all)
        val selectAllIcon = remember { "character.textbox" }

        LaunchedEffect(state.status, layoutCoords) {
            val open = state.status as? ContextMenuState.Status.Open ?: return@LaunchedEffect
            val coords = layoutCoords ?: return@LaunchedEffect

            if (MacNativeTextContextMenu.shouldSuppressSecondaryClickOpen()) {
                pendingNsEventAddress = 0L
                state.status = ContextMenuState.Status.Closed
                return@LaunchedEffect
            }

            val rootTopLeft = coords.localToRoot(open.rect.topLeft)
            val rootRect = Rect(rootTopLeft, open.rect.size)

            val dens = currentDensity.density.toDouble()
            val rectInWindowPts = Rect(
                left = (rootRect.left / dens).toFloat(),
                top = (rootRect.top / dens).toFloat(),
                right = (rootRect.right / dens).toFloat(),
                bottom = (rootRect.bottom / dens).toFloat(),
            )

            val nsEventAddr = pendingNsEventAddress
            pendingNsEventAddress = 0L

            val tm = currentTextManager
            val selectedText = tm.selectedText.text
            val userWriteBack = currentWriteBack
            val tmPaste = tm.paste
            val extras = currentShowExtraOptions

            val selectAllAction = ContextMenuAction(
                label = selectAllText,
                systemImageName = selectAllIcon,
                enabled = tm.selectAll != null,
                action = tm.selectAll ?: {},
            )

            val onTextChange: ((String) -> Unit)? = when {
                userWriteBack != null ->
                    { repl -> userWriteBack(selectedText, repl) }
                tmPaste != null ->
                    { repl -> if (repl != selectedText) applyViaClipboard(repl, tmPaste) }
                else -> null
            }

            dispatchAsyncOnMain {
                MacNativeTextContextMenu.showForTextField(
                    selectedText = selectedText,
                    nsEventAddress = nsEventAddr,
                    contentHeightPts = currentWindowInfo.containerSize.height.toDouble() / dens,
                    contentWidthPts = currentWindowInfo.containerSize.width.toDouble() / dens,
                    fieldRect = rectInWindowPts,
                    selectAllAction = selectAllAction,
                    customActions = currentCustomItems,
                    showExtraOptions = extras,
                    onTextChange = onTextChange,
                )
                state.status = ContextMenuState.Status.Closed
            }
        }

        Box(
            Modifier
                .onGloballyPositioned { layoutCoords = it }
                .contextMenuOpenDetector(
                    key = Pair(textManager, state)
                ) { pointerPosition ->
                    pendingNsEventAddress = captureCurrentNsEvent()

                    textManager.selectWordAtPositionIfNotAlreadySelected(pointerPosition)

                    val localRect = layoutCoords?.let {
                        Rect(Offset.Zero, it.size.toSize())
                    } ?: Rect(pointerPosition, 0f)

                    state.status = ContextMenuState.Status.Open(localRect)
                },
            propagateMinConstraints = true
        ) { content() }
    }

    internal fun applyViaClipboard(replacement: String, paste: () -> Unit) {
        try {
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val saved = try { clipboard.getContents(null) } catch (_: Throwable) { null }
            clipboard.setContents(StringSelection(replacement), null)
            SwingUtilities.invokeLater {
                try { paste() } finally {
                    if (saved != null) SwingUtilities.invokeLater {
                        try { clipboard.setContents(saved, null) } catch (_: Throwable) {}
                    }
                }
            }
        } catch (_: Throwable) {}
    }
}