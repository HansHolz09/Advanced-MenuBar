package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.hansholz.advancedmenubar.MenuModel.CheckboxItem
import dev.hansholz.advancedmenubar.MenuModel.CustomItem
import dev.hansholz.advancedmenubar.MenuModel.EditStd
import dev.hansholz.advancedmenubar.MenuModel.FileStd
import dev.hansholz.advancedmenubar.MenuModel.FormatStd
import dev.hansholz.advancedmenubar.MenuModel.HelpItem
import dev.hansholz.advancedmenubar.MenuModel.MenuElement
import dev.hansholz.advancedmenubar.MenuModel.SectionHeader
import dev.hansholz.advancedmenubar.MenuModel.Separator
import dev.hansholz.advancedmenubar.MenuModel.Submenu
import dev.hansholz.advancedmenubar.MenuModel.SystemItem
import dev.hansholz.advancedmenubar.MenuModel.TopMenu
import dev.hansholz.advancedmenubar.MenuModel.ViewStd
import dev.hansholz.advancedmenubar.MenuModel.WindowStd
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.util.Base64

@Composable
fun NativeMacMenuBar(
    appName: String,
    active: Boolean = true,
    content: MenuBarScope.() -> Unit,
) {
    val owner = remember { Any() }
    val composeScope = rememberCoroutineScope()
    val model = buildMenuModel(appName, content)

    SideEffect {
        NativeMenuCoordinator.update(
            owner = owner,
            active = active,
            appName = appName,
            model = model,
            dispatch = { action -> composeScope.launch { action() } },
        )
    }
    DisposableEffect(owner) {
        onDispose { NativeMenuCoordinator.remove(owner) }
    }
}

private object NativeMenuCoordinator {
    private data class Entry(
        val appName: String,
        val model: List<TopMenu>,
        val dispatch: ((() -> Unit) -> Unit),
    )

    private val entries = LinkedHashMap<Any, Entry>()
    private var activeOwner: Any? = null
    private var savedMenu = 0L
    private var installedMenu = 0L
    private var installedPayload: ByteArray? = null

    @Synchronized
    fun update(
        owner: Any,
        active: Boolean,
        appName: String,
        model: List<TopMenu>,
        dispatch: ((() -> Unit) -> Unit),
    ) {
        val entry = Entry(appName, model, dispatch)
        entries[owner] = entry
        if (!NativeMenuBridge.isAvailable) return
        if (savedMenu == 0L) savedMenu = NativeMenuBridge.retainMainMenu()
        if (active || activeOwner == null) activeOwner = owner
        if (activeOwner == owner) install(entry)
    }

    @Synchronized
    fun remove(owner: Any) {
        entries.remove(owner)
        if (activeOwner == owner) {
            activeOwner = entries.keys.lastOrNull()
            val replacement = activeOwner?.let(entries::get)
            if (replacement != null) {
                install(replacement)
            } else {
                NativeMenuBridge.restoreMainMenu(savedMenu)
                NativeMenuBridge.release(installedMenu)
                NativeMenuBridge.release(savedMenu)
                NativeMenuBridge.clearCallbacks()
                savedMenu = 0
                installedMenu = 0
                installedPayload = null
            }
        }
    }

    private fun install(entry: Entry) {
        val encoded = NativeMenuEncoder.encode(entry.appName, entry.model)
        val next =
            EncodedMenu(
                payload = encoded.payload,
                callbacks =
                    encoded.callbacks.mapValues { (_, action) ->
                        { entry.dispatch(action) }
                    },
            )
        if (installedPayload?.contentEquals(next.payload) == true) {
            NativeMenuBridge.updateCallbacks(next.callbacks)
            return
        }
        val handle = NativeMenuBridge.install(next)
        if (handle == 0L) return
        NativeMenuBridge.release(installedMenu)
        installedMenu = handle
        installedPayload = next.payload
    }
}

internal object NativeMenuEncoder {
    private const val MAGIC = 0x414D4231
    private const val VERSION = 1

    fun encode(
        appName: String,
        menus: List<TopMenu>,
    ): EncodedMenu {
        val nodes = mutableListOf<NativeNode>()
        menus.forEach { top ->
            val kind =
                when (top) {
                    is TopMenu.Application -> NodeKind.APPLICATION
                    is TopMenu.Edit -> if (top.suppressAutomaticItems) NodeKind.EDIT_SUPPRESSED else NodeKind.EDIT
                    is TopMenu.Window -> if (top.suppressAutoWindowList) NodeKind.MENU else NodeKind.WINDOW
                    is TopMenu.Help -> NodeKind.HELP
                    else -> NodeKind.MENU
                }
            val title = top.title(appName)
            val topIndex = nodes.size
            nodes += NativeNode(kind = kind, parent = -1, title = title)
            top.menuElements().forEach { appendElement(nodes, topIndex, it) }
        }

        val callbacks = LinkedHashMap<Long, () -> Unit>()
        val bytes = ByteArrayOutputStream()
        DataOutputStream(bytes).use { output ->
            output.writeInt(MAGIC)
            output.writeInt(VERSION)
            output.writeInt(nodes.size)
            nodes.forEachIndexed { index, node ->
                val actionId =
                    if (node.target == Target.CALLBACK && node.callback != null) {
                        (index + 1).toLong().also { callbacks[it] = node.callback }
                    } else {
                        node.actionValue
                    }
                output.writeInt(node.kind)
                output.writeInt(node.parent)
                output.writeString(node.title)
                output.writeString(node.selector)
                output.writeString(node.keyEquivalent)
                output.writeInt(node.modifiers)
                output.writeBoolean(node.enabled)
                output.writeInt(node.state)
                output.writeInt(node.target)
                output.writeLong(actionId)
                output.writeInt(node.iconKind)
                output.writeString(node.iconValue)
                output.writeBoolean(node.iconTemplate)
                output.writeString(node.subtitle)
                output.writeString(node.tooltip)
                output.writeString(node.badge)
            }
        }
        return EncodedMenu(bytes.toByteArray(), callbacks)
    }

    private fun appendElement(
        nodes: MutableList<NativeNode>,
        parent: Int,
        element: MenuElement,
    ) {
        when (element) {
            Separator -> nodes += NativeNode(NodeKind.SEPARATOR, parent)
            is SectionHeader -> nodes += NativeNode(NodeKind.SECTION_HEADER, parent, element.title)
            is Submenu -> appendSubmenu(nodes, parent, element.title, element.enabled, element.icon, element.children)
            is FileStd.OpenRecent -> appendSubmenu(nodes, parent, element.title, element.enabled, element.icon, element.children)
            is SystemItem.Services -> nodes += NativeNode(NodeKind.SERVICES, parent, element.title)
            is CustomItem ->
                nodes +=
                    NativeNode.item(
                        parent = parent,
                        title = element.title,
                        enabled = element.enabled,
                        icon = element.icon,
                        shortcut = element.shortcut.nativeShortcut(),
                        target = Target.CALLBACK,
                        callback = element.onClick,
                        subtitle = element.subtitle,
                        tooltip = element.tooltip,
                        badge = element.badge,
                    )
            is CheckboxItem ->
                nodes +=
                    NativeNode.item(
                        parent = parent,
                        title = element.title,
                        enabled = element.enabled,
                        icon = element.icon,
                        shortcut = element.shortcut.nativeShortcut(),
                        target = Target.CALLBACK,
                        callback = { element.onToggle(!element.checked) },
                        state = if (element.checked) 1 else 0,
                        subtitle = element.subtitle,
                        tooltip = element.tooltip,
                        badge = element.badge,
                    )
            else -> element.standardNode(parent)?.let(nodes::add)
        }
    }

    private fun appendSubmenu(
        nodes: MutableList<NativeNode>,
        parent: Int,
        title: String,
        enabled: Boolean,
        icon: MenuIcon?,
        children: List<MenuElement>,
    ) {
        val index = nodes.size
        nodes += NativeNode.submenu(parent, title, enabled, icon)
        children.forEach { appendElement(nodes, index, it) }
    }

    private fun DataOutputStream.writeString(value: String?) {
        if (value == null) {
            writeInt(-1)
            return
        }
        val encoded = value.toByteArray(Charsets.UTF_8)
        writeInt(encoded.size)
        write(encoded)
    }
}

private fun TopMenu.title(appName: String): String =
    when (this) {
        is TopMenu.Application -> appName
        is TopMenu.File -> title
        is TopMenu.Edit -> title
        is TopMenu.Format -> title
        is TopMenu.View -> title
        is TopMenu.Window -> title
        is TopMenu.Help -> title
        is TopMenu.Custom -> title
    }

private fun TopMenu.menuElements(): List<MenuElement> =
    when (this) {
        is TopMenu.Application -> elements
        is TopMenu.File -> elements
        is TopMenu.Edit -> elements
        is TopMenu.Format -> elements
        is TopMenu.View -> elements
        is TopMenu.Window -> elements
        is TopMenu.Help -> elements
        is TopMenu.Custom -> elements
    }

private object NodeKind {
    const val APPLICATION = 1
    const val MENU = 2
    const val WINDOW = 3
    const val HELP = 4
    const val EDIT = 5
    const val EDIT_SUPPRESSED = 6
    const val ITEM = 10
    const val SUBMENU = 11
    const val SEPARATOR = 12
    const val SECTION_HEADER = 13
    const val SERVICES = 14
}

private object Target {
    const val RESPONDER = 0
    const val APPLICATION = 1
    const val CALLBACK = 2
    const val EDIT_BRIDGE = 3
}

private data class NativeShortcut(
    val key: String,
    val modifiers: Int,
)

private data class NativeNode(
    val kind: Int,
    val parent: Int,
    val title: String = "",
    val selector: String? = null,
    val keyEquivalent: String? = null,
    val modifiers: Int = 0,
    val enabled: Boolean = true,
    val state: Int = -1,
    val target: Int = Target.RESPONDER,
    val actionValue: Long = 0,
    val callback: (() -> Unit)? = null,
    val iconKind: Int = 0,
    val iconValue: String? = null,
    val iconTemplate: Boolean = true,
    val subtitle: String? = null,
    val tooltip: String? = null,
    val badge: String? = null,
) {
    companion object {
        fun submenu(
            parent: Int,
            title: String,
            enabled: Boolean,
            icon: MenuIcon?,
        ): NativeNode {
            val nativeIcon = icon.nativeIcon()
            return NativeNode(
                kind = NodeKind.SUBMENU,
                parent = parent,
                title = title,
                enabled = enabled,
                iconKind = nativeIcon.kind,
                iconValue = nativeIcon.value,
                iconTemplate = nativeIcon.template,
            )
        }

        fun item(
            parent: Int,
            title: String,
            enabled: Boolean,
            icon: MenuIcon?,
            shortcut: NativeShortcut? = null,
            selector: String? = null,
            target: Int = Target.RESPONDER,
            actionValue: Long = 0,
            callback: (() -> Unit)? = null,
            state: Int = -1,
            subtitle: String? = null,
            tooltip: String? = null,
            badge: String? = null,
        ): NativeNode {
            val nativeIcon = icon.nativeIcon()
            return NativeNode(
                kind = NodeKind.ITEM,
                parent = parent,
                title = title,
                selector = selector,
                keyEquivalent = shortcut?.key,
                modifiers = shortcut?.modifiers ?: 0,
                enabled = enabled,
                state = state,
                target = target,
                actionValue = actionValue,
                callback = callback,
                iconKind = nativeIcon.kind,
                iconValue = nativeIcon.value,
                iconTemplate = nativeIcon.template,
                subtitle = subtitle,
                tooltip = tooltip,
                badge = badge,
            )
        }
    }
}

private data class NativeIcon(
    val kind: Int,
    val value: String?,
    val template: Boolean,
)

private fun MenuIcon?.nativeIcon(): NativeIcon =
    when (this) {
        null -> NativeIcon(0, null, true)
        is MenuIcon.SFSymbol -> NativeIcon(1, name, template)
        is MenuIcon.File -> NativeIcon(2, path, template)
        is MenuIcon.Png -> NativeIcon(3, Base64.getEncoder().encodeToString(bytes), template)
    }

private fun MenuShortcut?.nativeShortcut(): NativeShortcut? {
    val cocoa = this?.toCocoa() ?: return null
    return NativeShortcut(cocoa.first, cocoa.second.toInt())
}

private fun shortcut(
    key: String,
    shift: Boolean = false,
    option: Boolean = false,
    control: Boolean = false,
): NativeShortcut {
    var modifiers = 1 shl 20
    if (shift) modifiers = modifiers or (1 shl 17)
    if (option) modifiers = modifiers or (1 shl 19)
    if (control) modifiers = modifiers or (1 shl 18)
    return NativeShortcut(key, modifiers)
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
private fun MenuElement.standardNode(parent: Int): NativeNode? {
    fun standard(
        title: String,
        enabled: Boolean,
        icon: MenuIcon?,
        selector: String,
        shortcut: NativeShortcut? = null,
        target: Int = Target.RESPONDER,
        override: (() -> Unit)? = null,
    ): NativeNode =
        NativeNode.item(
            parent = parent,
            title = title,
            enabled = enabled,
            icon = icon,
            shortcut = shortcut,
            selector = if (override == null) selector else null,
            target = if (override == null) target else Target.CALLBACK,
            callback = override,
        )

    fun edit(
        title: String,
        enabled: Boolean,
        icon: MenuIcon?,
        command: Int,
        override: (() -> Unit)?,
        overrideShortcut: NativeShortcut,
    ): NativeNode =
        NativeNode.item(
            parent = parent,
            title = title,
            enabled = enabled,
            icon = icon,
            shortcut = overrideShortcut,
            target = if (override == null) Target.EDIT_BRIDGE else Target.CALLBACK,
            actionValue = command.toLong(),
            callback = override,
        )

    fun toggle(
        title: String,
        enabled: Boolean,
        icon: MenuIcon?,
        selector: String,
        checked: Boolean?,
        onToggle: ((Boolean) -> Unit)?,
        shortcut: NativeShortcut? = null,
    ): NativeNode =
        NativeNode.item(
            parent = parent,
            title = title,
            enabled = enabled,
            icon = icon,
            shortcut = shortcut,
            selector = if (onToggle == null) selector else null,
            target = if (onToggle == null) Target.RESPONDER else Target.CALLBACK,
            callback = onToggle?.let { callback -> { callback(!(checked ?: false)) } },
            state = checked?.let { if (it) 1 else 0 } ?: -1,
        )

    return when (this) {
        is SystemItem.About ->
            standard(
                title,
                enabled,
                icon,
                "orderFrontStandardAboutPanel:",
                target = Target.APPLICATION,
                override = onClick,
            )
        is SystemItem.Settings -> standard(title, enabled, icon, "showPreferences:", shortcut(","), Target.APPLICATION, onClick)
        is SystemItem.Hide -> standard(title, enabled, icon, "hide:", shortcut("h"), Target.APPLICATION, onClick)
        is SystemItem.HideOthers ->
            standard(
                title,
                enabled,
                icon,
                "hideOtherApplications:",
                shortcut("h", option = true),
                Target.APPLICATION,
                onClick,
            )
        is SystemItem.ShowAll -> standard(title, enabled, icon, "unhideAllApplications:", target = Target.APPLICATION, override = onClick)
        is SystemItem.Quit -> standard(title, enabled, icon, "terminate:", shortcut("q"), Target.APPLICATION, onClick)
        is FileStd.New -> standard(title, enabled, icon, "newDocument:", shortcut("n"), override = onClick)
        is FileStd.Open -> standard(title, enabled, icon, "openDocument:", shortcut("o"), override = onClick)
        is FileStd.Close -> standard(title, enabled, icon, "performClose:", shortcut("w"), override = onClick)
        is FileStd.CloseAll -> standard(title, enabled, icon, "closeAllDocuments:", shortcut("w", option = true), override = onClick)
        is FileStd.Save -> standard(title, enabled, icon, "saveDocument:", shortcut("s"), override = onClick)
        is FileStd.SaveAs -> standard(title, enabled, icon, "saveDocumentAs:", shortcut("s", shift = true), override = onClick)
        is FileStd.Duplicate -> standard(title, enabled, icon, "duplicateDocument:", override = onClick)
        is FileStd.Rename -> standard(title, enabled, icon, "renameDocument:", override = onClick)
        is FileStd.MoveTo -> standard(title, enabled, icon, "moveDocument:", override = onClick)
        is FileStd.PageSetup -> standard(title, enabled, icon, "runPageLayout:", shortcut("p", shift = true), override = onClick)
        is FileStd.Print -> standard(title, enabled, icon, "printDocument:", shortcut("p"), override = onClick)
        is FileStd.ClearRecent -> standard(title, enabled, icon, "clearRecentDocuments:", override = onClick)
        is EditStd.Undo -> edit(title, enabled, icon, EditCommand.UNDO, onClick, shortcut("z"))
        is EditStd.Redo -> edit(title, enabled, icon, EditCommand.REDO, onClick, shortcut("z", shift = true))
        is EditStd.Cut -> edit(title, enabled, icon, EditCommand.CUT, onClick, shortcut("x"))
        is EditStd.Copy -> edit(title, enabled, icon, EditCommand.COPY, onClick, shortcut("c"))
        is EditStd.Paste -> edit(title, enabled, icon, EditCommand.PASTE, onClick, shortcut("v"))
        is EditStd.PasteAndMatchStyle ->
            edit(
                title,
                enabled,
                icon,
                EditCommand.PASTE_MATCH_STYLE,
                onClick,
                shortcut("v", shift = true, option = true),
            )
        is EditStd.Delete -> edit(title, enabled, icon, EditCommand.DELETE, onClick, NativeShortcut("\u007F", 0))
        is EditStd.SelectAll -> edit(title, enabled, icon, EditCommand.SELECT_ALL, onClick, shortcut("a"))
        is EditStd.Find -> edit(title, enabled, icon, EditCommand.FIND, onClick, shortcut("f"))
        is EditStd.FindAndReplace -> edit(title, enabled, icon, EditCommand.FIND_REPLACE, onClick, shortcut("f", option = true))
        is EditStd.FindNext -> edit(title, enabled, icon, EditCommand.FIND_NEXT, onClick, shortcut("g"))
        is EditStd.FindPrevious -> edit(title, enabled, icon, EditCommand.FIND_PREVIOUS, onClick, shortcut("g", shift = true))
        is EditStd.UseSelectionForFind -> edit(title, enabled, icon, EditCommand.USE_SELECTION_FOR_FIND, onClick, shortcut("e"))
        is EditStd.JumpToSelection -> edit(title, enabled, icon, EditCommand.JUMP_TO_SELECTION, onClick, shortcut("j"))
        is EditStd.ToggleSmartQuotes -> toggle(title, enabled, icon, "toggleAutomaticQuoteSubstitution:", checked, onToggle)
        is EditStd.ToggleSmartDashes -> toggle(title, enabled, icon, "toggleAutomaticDashSubstitution:", checked, onToggle)
        is EditStd.ToggleLinkDetection -> toggle(title, enabled, icon, "toggleAutomaticLinkDetection:", checked, onToggle)
        is EditStd.ToggleTextReplacement -> toggle(title, enabled, icon, "toggleAutomaticTextReplacement:", checked, onToggle)
        is EditStd.ToggleSpellingCorrection -> toggle(title, enabled, icon, "toggleAutomaticSpellingCorrection:", checked, onToggle)
        is EditStd.Uppercase -> standard(title, enabled, icon, "uppercaseWord:", override = onClick)
        is EditStd.Lowercase -> standard(title, enabled, icon, "lowercaseWord:", override = onClick)
        is EditStd.Capitalize -> standard(title, enabled, icon, "capitalizeWord:", override = onClick)
        is EditStd.StartSpeaking -> standard(title, enabled, icon, "startSpeaking:", override = onClick)
        is EditStd.StopSpeaking -> standard(title, enabled, icon, "stopSpeaking:", override = onClick)
        is FormatStd.ShowFonts -> standard(title, enabled, icon, "orderFrontFontPanel:", shortcut("t"), override = onClick)
        is FormatStd.ShowColors -> standard(title, enabled, icon, "orderFrontColorPanel:", override = onClick)
        is FormatStd.Bold -> toggle(title, enabled, icon, "toggleBoldface:", checked, onToggle, shortcut("b"))
        is FormatStd.Italic -> toggle(title, enabled, icon, "toggleItalics:", checked, onToggle, shortcut("i"))
        is FormatStd.Underline -> toggle(title, enabled, icon, "toggleUnderline:", checked, onToggle, shortcut("u"))
        is FormatStd.Bigger -> standard(title, enabled, icon, "makeTextBigger:", shortcut("="), override = onClick)
        is FormatStd.Smaller -> standard(title, enabled, icon, "makeTextSmaller:", shortcut("-"), override = onClick)
        is FormatStd.KerningStandard -> standard(title, enabled, icon, "useStandardKerning:", override = onClick)
        is FormatStd.KerningNone -> standard(title, enabled, icon, "turnOffKerning:", override = onClick)
        is FormatStd.KerningTighten -> standard(title, enabled, icon, "tightenKerning:", override = onClick)
        is FormatStd.KerningLoosen -> standard(title, enabled, icon, "loosenKerning:", override = onClick)
        is FormatStd.LigaturesNone -> standard(title, enabled, icon, "turnOffLigatures:", override = onClick)
        is FormatStd.LigaturesStandard -> standard(title, enabled, icon, "useStandardLigatures:", override = onClick)
        is FormatStd.LigaturesAll -> standard(title, enabled, icon, "useAllLigatures:", override = onClick)
        is FormatStd.BaselineStandard -> standard(title, enabled, icon, "unscript:", override = onClick)
        is FormatStd.RaiseBaseline -> standard(title, enabled, icon, "raiseBaseline:", override = onClick)
        is FormatStd.LowerBaseline -> standard(title, enabled, icon, "lowerBaseline:", override = onClick)
        is FormatStd.Superscript -> standard(title, enabled, icon, "superscript:", override = onClick)
        is FormatStd.Subscript -> standard(title, enabled, icon, "subscript:", override = onClick)
        is FormatStd.AlignLeft -> toggle(title, enabled, icon, "alignLeft:", checked, onToggle)
        is FormatStd.AlignCenter -> toggle(title, enabled, icon, "alignCenter:", checked, onToggle)
        is FormatStd.AlignRight -> toggle(title, enabled, icon, "alignRight:", checked, onToggle)
        is FormatStd.AlignJustified -> toggle(title, enabled, icon, "alignJustified:", checked, onToggle)
        is ViewStd.ShowToolbar -> toggle(title, enabled, icon, "toggleToolbarShown:", checked, onToggle, shortcut("t", option = true))
        is ViewStd.CustomizeToolbar -> standard(title, enabled, icon, "runToolbarCustomizationPalette:", override = onClick)
        is ViewStd.ToggleFullScreen ->
            standard(
                title,
                enabled,
                icon,
                "toggleFullScreen:",
                shortcut("f", control = true),
                override = onClick,
            )
        is ViewStd.ToggleSidebar -> toggle(title, enabled, icon, "toggleSidebar:", checked, onToggle, shortcut("s", option = true))
        is ViewStd.ToggleTabBar -> toggle(title, enabled, icon, "toggleTabBar:", checked, onToggle, shortcut("t", shift = true))
        is WindowStd.Close -> standard(title, enabled, icon, "performClose:", shortcut("w"), override = onClick)
        is WindowStd.Minimize -> standard(title, enabled, icon, "performMiniaturize:", shortcut("m"), override = onClick)
        is WindowStd.MinimizeAll ->
            standard(
                title,
                enabled,
                icon,
                "miniaturizeAll:",
                shortcut("m", option = true),
                Target.APPLICATION,
                onClick,
            )
        is WindowStd.Zoom -> standard(title, enabled, icon, "performZoom:", override = onClick)
        is WindowStd.BringAllToFront -> standard(title, enabled, icon, "arrangeInFront:", target = Target.APPLICATION, override = onClick)
        is WindowStd.ShowNextTab -> standard(title, enabled, icon, "selectNextTab:", override = onClick)
        is WindowStd.ShowPreviousTab -> standard(title, enabled, icon, "selectPreviousTab:", override = onClick)
        is WindowStd.MergeAllWindows -> standard(title, enabled, icon, "mergeAllWindows:", override = onClick)
        is WindowStd.MoveTabToNewWindow -> standard(title, enabled, icon, "moveTabToNewWindow:", override = onClick)
        is HelpItem.AppHelp -> standard(title, enabled, icon, "showHelp:", shortcut("?", shift = true), Target.APPLICATION, onClick)
        else -> null
    }
}

private object EditCommand {
    const val UNDO = 0
    const val REDO = 1
    const val CUT = 2
    const val COPY = 3
    const val PASTE = 4
    const val PASTE_MATCH_STYLE = 5
    const val DELETE = 6
    const val SELECT_ALL = 7
    const val FIND = 8
    const val FIND_REPLACE = 9
    const val FIND_NEXT = 10
    const val FIND_PREVIOUS = 11
    const val USE_SELECTION_FOR_FIND = 12
    const val JUMP_TO_SELECTION = 13
}
