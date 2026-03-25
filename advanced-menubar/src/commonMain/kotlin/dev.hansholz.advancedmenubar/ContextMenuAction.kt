package dev.hansholz.advancedmenubar

data class ContextMenuAction(val label: String, val systemImageName: String? = null, val enabled: Boolean = true, val action: () -> Unit)