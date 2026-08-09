package dev.hansholz.advancedmenubar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import dev.hansholz.advancedmenubar.NativeTextContextMenuBridge.dispatchAsyncOnMain
import org.jetbrains.skiko.hostOs

/**
 * Replaces Compose text-field popups below [content] with the native macOS text context menu.
 *
 * On Windows, Linux, or when the packaged JNI bridge cannot be loaded, this provider leaves the
 * normal Compose context menu untouched. On macOS, [isDark] updates the appearance of the entire
 * `NSApplication`, ensuring that application, context, and other native menus use the same theme.
 * Pass `null` to leave the current application appearance unchanged.
 *
 * @param isDark dark/light application appearance, or `null` to make no appearance change.
 * @param showExtraOptions whether AppKit text services beyond Cut, Copy, and Paste remain visible.
 * @param customActions application actions inserted after the standard selection actions.
 * @param content content whose Compose text fields should use the provider.
 */
@Composable
fun NativeTextContextMenuProvider(
    isDark: Boolean? = isSystemInDarkTheme(),
    showExtraOptions: Boolean = true,
    customActions: List<ContextMenuAction> = emptyList(),
    content: @Composable () -> Unit,
) {
    if (hostOs.isMacOS && NativeMenuBridge.isAvailable) {
        isDark?.let {
            LaunchedEffect(it) {
                dispatchAsyncOnMain {
                    NativeTextContextMenuBridge.applyAppearance(it)
                }
            }
        }
        val contextMenu =
            remember(showExtraOptions, customActions) {
                NSMenuTextContextMenu(showExtraOptions, customActions)
            }
        @OptIn(ExperimentalFoundationApi::class)
        CompositionLocalProvider(LocalTextContextMenu provides contextMenu) {
            content()
        }
    } else {
        content()
    }
}
