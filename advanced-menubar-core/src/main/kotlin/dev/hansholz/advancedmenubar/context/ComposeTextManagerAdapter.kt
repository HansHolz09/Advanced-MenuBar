package dev.hansholz.advancedmenubar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.text.TextContextMenu
import androidx.compose.ui.text.AnnotatedString
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * Bridges AppKit text replacements to Compose's clipboard-event entry point without changing the
 * system clipboard. The legacy [TextContextMenu] API does not expose arbitrary replacement
 * directly, so the accessor is resolved once for Compose's private desktop text-manager wrapper.
 */
@OptIn(ExperimentalFoundationApi::class)
internal class ComposeTextManagerAdapter private constructor(
    private val textManager: TextContextMenu.TextManager,
    private val accessor: Accessor?,
) {
    val isEditable: Boolean
        get() {
            val resolved = accessor ?: return textManager.paste != null
            val owner = resolved.owner(textManager) ?: return textManager.paste != null
            return resolved.editableMethod?.invokeSafely(owner) as? Boolean ?: (textManager.paste != null)
        }

    fun replaceSelectedText(value: String): Boolean {
        val resolved = accessor ?: return false
        val owner = resolved.owner(textManager) ?: return false
        val method = resolved.replacementMethod ?: return false
        val succeeded = method.invokeSafely(owner, AnnotatedString(value)) != InvocationFailed
        if (debugEnabled) {
            System.err.println(
                "[AdvancedMenubar] compose-text-replacement " +
                    "method=${method.name} succeeded=$succeeded",
            )
        }
        return succeeded
    }

    private data class Accessor(
        val ownerField: Field,
        val editableMethod: Method?,
        val replacementMethod: Method?,
    ) {
        fun owner(textManager: TextContextMenu.TextManager): Any? = ownerField.getSafely(textManager)
    }

    companion object {
        private val accessors = ConcurrentHashMap<Class<*>, Accessor?>()
        private val InvocationFailed = Any()
        private val debugEnabled =
            System.getenv("ADVANCED_MENUBAR_DEBUG")?.let { it.isNotEmpty() && it != "0" } == true

        fun create(textManager: TextContextMenu.TextManager): ComposeTextManagerAdapter =
            ComposeTextManagerAdapter(
                textManager = textManager,
                accessor = accessors.computeIfAbsent(textManager.javaClass, ::resolveAccessor),
            )

        private fun resolveAccessor(wrapperClass: Class<*>): Accessor? {
            val ownerField =
                wrapperClass.declaredFields
                    .firstOrNull { it.name == "${'$'}this_textManager" }
                    ?.takeIf { it.trySetAccessible() }
                    ?: return null
            val ownerClass = ownerField.type
            val editableMethod =
                ownerClass.methods.firstOrNull {
                    it.parameterCount == 0 &&
                        (it.name == "getEditable" || it.name.startsWith("getEditable$"))
                }
            val annotatedStringParameter = arrayOf<Class<*>>(AnnotatedString::class.java)
            val replacementMethod =
                ownerClass.methods.firstOrNull {
                    it.name.startsWith("onPasteEvent") &&
                        it.parameterTypes.contentEquals(annotatedStringParameter)
                } ?: ownerClass.methods.firstOrNull {
                    it.name.startsWith("paste$") &&
                        it.parameterTypes.contentEquals(annotatedStringParameter)
                }
            return Accessor(ownerField, editableMethod, replacementMethod)
        }

        private fun Field.getSafely(receiver: Any): Any? = runCatching { get(receiver) }.getOrNull()

        private fun Method.invokeSafely(
            receiver: Any,
            vararg arguments: Any,
        ): Any? = runCatching { invoke(receiver, *arguments) }.getOrElse { InvocationFailed }
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun TextContextMenu.TextManager.composeAdapter(): ComposeTextManagerAdapter = ComposeTextManagerAdapter.create(this)
