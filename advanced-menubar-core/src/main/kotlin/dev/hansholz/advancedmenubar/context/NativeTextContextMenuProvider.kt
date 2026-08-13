package dev.hansholz.advancedmenubar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.text.LocalTextContextMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.PlatformTextInputInterceptor
import dev.hansholz.advancedmenubar.NativeTextContextMenuBridge.dispatchAsyncOnMain
import org.jetbrains.skiko.hostOs

private val LocalEditableTextInputActive = staticCompositionLocalOf<State<Boolean>?> { null }

/** Returns whether default Edit commands should be enabled for the current provider scope. */
@InternalAdvancedMenuBarApi
@Composable
fun defaultEditMenuEnabled(): Boolean = LocalEditableTextInputActive.current?.value ?: true

/**
 * Replaces Compose text popups below [content] with the matching native macOS text context menu.
 * Editable fields use an editable AppKit text view; selectable read-only content uses a
 * non-editable one.
 *
 * On Windows, Linux, or when the packaged JNI bridge cannot be loaded, this provider leaves the
 * normal Compose context menu untouched. On macOS, [isDark] updates the appearance of the entire
 * `NSApplication`, ensuring that application, context, and other native menus use the same theme.
 * Pass `null` to leave the current application appearance unchanged.
 *
 * @param isDark dark/light application appearance, or `null` to make no appearance change.
 * @param showExtraOptions whether AppKit text services beyond Cut, Copy, and Paste remain visible.
 * @param customActions application actions inserted after the standard selection actions.
 * @param content content whose editable or selectable text should use the provider. A
 *   `DefaultMacMenuBar` declared in this scope disables its Edit commands while no editable text
 *   input session is active.
 */
@OptIn(ExperimentalComposeUiApi::class)
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
        var activeInputSessions by remember { mutableIntStateOf(0) }
        val editableTextInputActive =
            remember { derivedStateOf { activeInputSessions > 0 } }
        val textInputInterceptor =
            remember {
                PlatformTextInputInterceptor { request, nextHandler ->
                    activeInputSessions++
                    try {
                        nextHandler.startInputMethod(request)
                    } finally {
                        activeInputSessions = (activeInputSessions - 1).coerceAtLeast(0)
                    }
                }
            }
        @OptIn(ExperimentalFoundationApi::class)
        CompositionLocalProvider(
            LocalTextContextMenu provides contextMenu,
            LocalEditableTextInputActive provides editableTextInputActive,
        ) {
            InterceptPlatformTextInput(textInputInterceptor, content)
        }
    } else {
        content()
    }
}
