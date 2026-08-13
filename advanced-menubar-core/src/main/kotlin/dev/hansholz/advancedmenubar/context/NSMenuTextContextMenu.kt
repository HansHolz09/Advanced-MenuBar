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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.toSize
import composeadvancedmenubar.generated.resources.Res
import composeadvancedmenubar.generated.resources.edit_select_all
import dev.hansholz.advancedmenubar.NativeTextContextMenuBridge.captureCurrentNsEvent
import dev.hansholz.advancedmenubar.NativeTextContextMenuBridge.dispatchAsyncOnMain
import dev.hansholz.advancedmenubar.utils.majorSystemVersion
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalFoundationApi::class)
internal class NSMenuTextContextMenu(
    private val showExtraOptions: Boolean,
    private val customItems: List<ContextMenuAction>,
) : TextContextMenu {
    @Composable
    override fun Area(
        textManager: TextContextMenu.TextManager,
        state: ContextMenuState,
        content: @Composable () -> Unit,
    ) {
        val density = LocalDensity.current
        val windowInfo = LocalWindowInfo.current
        val composeScope = rememberCoroutineScope()

        val currentTextManager by rememberUpdatedState(textManager)
        val currentShowExtraOptions by rememberUpdatedState(showExtraOptions)
        val currentDensity by rememberUpdatedState(density)
        val currentWindowInfo by rememberUpdatedState(windowInfo)
        val currentCustomItems by rememberUpdatedState(customItems)

        var layoutCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var pendingNsEventAddress by remember { mutableStateOf(0L) }

        val selectAllText = stringResource(Res.string.edit_select_all)
        val selectAllIcon = remember { "character.textbox" }

        LaunchedEffect(state.status, layoutCoords) {
            val open = state.status as? ContextMenuState.Status.Open ?: return@LaunchedEffect
            val coords = layoutCoords ?: return@LaunchedEffect

            withFrameNanos { }

            val rootTopLeft = coords.localToRoot(open.rect.topLeft)
            val rootRect = Rect(rootTopLeft, open.rect.size)

            val dens = currentDensity.density.toDouble()
            val rectInWindowPts =
                Rect(
                    left = (rootRect.left / dens).toFloat(),
                    top = (rootRect.top / dens).toFloat(),
                    right = (rootRect.right / dens).toFloat(),
                    bottom = (rootRect.bottom / dens).toFloat(),
                )

            val nsEventAddr = pendingNsEventAddress
            pendingNsEventAddress = 0L

            val tm = currentTextManager
            val selectedText = tm.selectedText.text
            val tmPaste = tm.paste
            val tmSelectAll = tm.selectAll
            val extras = currentShowExtraOptions
            val composeAdapter = tm.composeAdapter()
            val isEditable = composeAdapter.isEditable

            val selectAllAction =
                ContextMenuAction(
                    label = selectAllText,
                    systemImageName = if (majorSystemVersion == 26) selectAllIcon else null,
                    enabled = tmSelectAll?.enabled == true,
                    action = tmSelectAll?.execute ?: {},
                )

            val onTextChange: ((String) -> Unit)? =
                tmPaste?.takeIf { isEditable }?.let { paste ->
                    { replacement ->
                        if (replacement != selectedText) {
                            if (!composeAdapter.replaceSelectedText(replacement)) {
                                NativeTextContextMenuBridge.applyViaClipboard(replacement, paste.execute)
                            }
                        }
                    }
                }

            dispatchAsyncOnMain {
                NativeTextContextMenuBridge.showForTextField(
                    selectedText = selectedText,
                    isEditable = isEditable,
                    nsEventAddress = nsEventAddr,
                    contentHeightPts = currentWindowInfo.containerSize.height.toDouble() / dens,
                    contentWidthPts = currentWindowInfo.containerSize.width.toDouble() / dens,
                    fieldRect = rectInWindowPts,
                    selectAllAction = selectAllAction,
                    customActions = currentCustomItems,
                    showExtraOptions = extras,
                    onTextChange = onTextChange,
                    dispatch = { action -> composeScope.launch { action() } },
                )
                state.status = ContextMenuState.Status.Closed
            }
        }

        Box(
            Modifier
                .onGloballyPositioned { layoutCoords = it }
                .contextMenuOpenDetector(
                    key = Pair(textManager, state),
                ) { pointerPosition ->
                    pendingNsEventAddress = captureCurrentNsEvent()
                    if (pendingNsEventAddress == 0L) return@contextMenuOpenDetector

                    textManager.selectWordAtPositionIfNotAlreadySelected(pointerPosition)

                    val localRect =
                        layoutCoords?.let {
                            Rect(Offset.Zero, it.size.toSize())
                        } ?: Rect(pointerPosition, 0f)

                    state.status = ContextMenuState.Status.Open(localRect)
                },
            propagateMinConstraints = true,
        ) { content() }
    }
}
