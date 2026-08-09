package dev.hansholz.advancedmenubar

/** A custom action inserted into a native macOS text context menu. */
data class ContextMenuAction(
    /** Visible menu label. */
    val label: String,
    /** Optional SF Symbol name. Invalid or unavailable symbols are omitted. */
    val systemImageName: String? = null,
    /** Whether the action can currently be selected. */
    val enabled: Boolean = true,
    /** Callback dispatched back to the Compose coroutine scope. */
    val action: () -> Unit,
)
