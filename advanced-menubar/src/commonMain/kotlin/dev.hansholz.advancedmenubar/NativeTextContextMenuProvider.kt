package dev.hansholz.advancedmenubar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import dev.hansholz.advancedmenubar.MacNativeTextContextMenu.dispatchAsyncOnMain
import org.jetbrains.skiko.hostOs

@Composable
fun NativeTextContextMenuProvider(
    isDark: Boolean? = isSystemInDarkTheme(),
    showExtraOptions: Boolean = true,
    customActions: List<ContextMenuAction> = emptyList(),
    content: @Composable () -> Unit,
) {
    if (hostOs.isMacOS) {
        isDark?.let {
            LaunchedEffect(it) {
                dispatchAsyncOnMain {
                    MacNativeTextContextMenu.applyAppearance(it)
                }
            }
        }
        NSMenuTextContextMenu.showExtraOptions = showExtraOptions
        NSMenuTextContextMenu.customItems = customActions
        @OptIn(ExperimentalFoundationApi::class)
        CompositionLocalProvider(LocalTextContextMenu provides NSMenuTextContextMenu) {
            content()
        }
    } else {
        content()
    }
}