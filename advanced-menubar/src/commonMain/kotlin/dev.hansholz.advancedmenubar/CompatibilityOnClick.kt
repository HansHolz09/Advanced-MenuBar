package dev.hansholz.advancedmenubar

import org.jetbrains.skiko.hostOs

fun compatibilityOnClick(
    onMacClick: (() -> Unit)? = null,
    onNonMacClick: (() -> Unit),
): (() -> Unit)? =
    if (hostOs.isMacOS && onMacClick != null) {
        onMacClick
    } else if (!hostOs.isMacOS) {
        onNonMacClick
    } else {
        null
    }