package dev.hansholz.advancedmenubar

import androidx.compose.ui.geometry.Rect
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

internal object NativeTextContextMenuBridge {
    private val nextCallbackId = AtomicLong(1)
    private val actions = ConcurrentHashMap<Long, () -> Unit>()
    private val textCallbacks = ConcurrentHashMap<Long, (String) -> Unit>()

    fun dispatchAsyncOnMain(block: () -> Unit) = block()

    fun applyAppearance(isDark: Boolean) {
        if (NativeMenuBridge.isAvailable) nativeApplyAppearance(isDark)
    }

    fun captureCurrentNsEvent(): Long = if (NativeMenuBridge.isAvailable) nativeCaptureCurrentEvent() else 0L

    fun showForTextField(
        selectedText: String,
        isEditable: Boolean,
        nsEventAddress: Long,
        contentHeightPts: Double,
        contentWidthPts: Double,
        fieldRect: Rect,
        selectAllAction: ContextMenuAction,
        customActions: List<ContextMenuAction>,
        showExtraOptions: Boolean,
        onTextChange: ((String) -> Unit)?,
        dispatch: ((() -> Unit) -> Unit),
    ) {
        if (!NativeMenuBridge.isAvailable) return
        actions.clear()
        textCallbacks.clear()
        val allActions =
            buildList {
                if (isEditable) add(selectAllAction)
                addAll(customActions)
            }
        val actionIds =
            LongArray(allActions.size) { index ->
                nextCallbackId.getAndIncrement().also {
                    val action = allActions[index].action
                    actions[it] = { dispatch(action) }
                }
            }
        val textCallbackId =
            onTextChange?.let { callback ->
                nextCallbackId.getAndIncrement().also {
                    textCallbacks[it] = { value -> dispatch { callback(value) } }
                }
            } ?: 0L

        nativeShowTextContextMenu(
            selectedText = selectedText,
            isEditable = isEditable,
            eventAddress = nsEventAddress,
            contentHeight = contentHeightPts,
            contentWidth = contentWidthPts,
            left = fieldRect.left.toDouble(),
            top = fieldRect.top.toDouble(),
            right = fieldRect.right.toDouble(),
            bottom = fieldRect.bottom.toDouble(),
            labels = allActions.map { it.label }.toTypedArray(),
            symbolNames = allActions.map { it.systemImageName }.toTypedArray(),
            enabled = BooleanArray(allActions.size) { allActions[it].enabled },
            actionIds = actionIds,
            customActionCount = customActions.size,
            showExtraOptions = showExtraOptions,
            textCallbackId = textCallbackId,
        )
    }

    fun applyViaClipboard(
        replacement: String,
        paste: () -> Unit,
    ) {
        if (!NativeMenuBridge.isAvailable) return
        val snapshot = nativeSnapshotClipboard()
        val expectedChangeCount = nativeSetClipboardString(replacement)
        try {
            paste()
        } finally {
            nativeRestoreClipboardLater(snapshot, expectedChangeCount)
        }
    }

    @JvmStatic
    fun onAction(callbackId: Long) {
        actions[callbackId]?.invoke()
    }

    @JvmStatic
    fun onTextChanged(
        callbackId: Long,
        value: String,
    ) {
        textCallbacks[callbackId]?.invoke(value)
    }

    @JvmStatic
    fun onDismissed() = Unit

    @JvmStatic private external fun nativeApplyAppearance(isDark: Boolean)

    @JvmStatic private external fun nativeCaptureCurrentEvent(): Long

    @JvmStatic
    private external fun nativeShowTextContextMenu(
        selectedText: String,
        isEditable: Boolean,
        eventAddress: Long,
        contentHeight: Double,
        contentWidth: Double,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double,
        labels: Array<String>,
        symbolNames: Array<String?>,
        enabled: BooleanArray,
        actionIds: LongArray,
        customActionCount: Int,
        showExtraOptions: Boolean,
        textCallbackId: Long,
    )

    @JvmStatic private external fun nativeSnapshotClipboard(): Long

    @JvmStatic private external fun nativeSetClipboardString(value: String): Long

    @JvmStatic
    private external fun nativeRestoreClipboardLater(
        snapshot: Long,
        expectedChangeCount: Long,
    )
}
