package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.window.FrameWindowScope
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.CheckboxItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.CustomItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.EditStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.FileStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.FormatStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.HelpItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.MenuElement
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.SectionHeader
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.Separator
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.Submenu
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.SystemItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.TopMenu
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.ViewStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.WindowStd
import java.awt.Font
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.io.ByteArrayInputStream
import java.util.WeakHashMap
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JCheckBoxMenuItem
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.KeyStroke
import javax.swing.SwingUtilities
import kotlin.collections.forEach

@Composable
fun FrameWindowScope.SwingMenuBar(
    appName: String = window.title,
    content: AdvancedMenuBarScope.() -> Unit
) {
    val model = MenuScopeToList(appName, content)
    SwingMenuBarImplementation(model)
}

@Composable
internal fun FrameWindowScope.SwingMenuBarImplementation(model: List<TopMenu>) {
    DisposableEffect(Unit) {
        onDispose {
            SwingUtilities.invokeLater {
                window.jMenuBar = null
            }
        }
    }
    LaunchedEffect(model) {
        SwingUtilities.invokeLater {
            window.jMenuBar = SwingMenuBarFromModel(model)
            window.invalidate()
            window.validate()
            window.repaint()
        }
    }
}

private fun SwingMenuBarFromModel(menus: List<TopMenu>): JMenuBar =
    JMenuBar().apply {
        menus.forEach { top ->
            when (top) {
                is TopMenu.Application -> Unit
                is TopMenu.File -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.Edit -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.Format -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.View -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.Window -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.Help -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
                is TopMenu.Custom -> add(JMenu(top.title).apply { addSwingElements(top.elements) })
            }
        }
    }

private fun List<MenuElement>.collapseSeparators(): List<MenuElement> {
    val out = ArrayList<MenuElement>(size)
    var lastWasSeparator = true
    for (e in this) {
        if (e is Separator) {
            if (!lastWasSeparator) {
                out.add(e)
                lastWasSeparator = true
            }
        } else {
            out.add(e)
            lastWasSeparator = false
        }
    }
    if (out.isNotEmpty() && out.last() is Separator) {
        out.removeAt(out.size - 1)
    }
    return out
}

private fun JMenu.addSwingElements(elements: List<MenuElement>) {
    elements.collapseSeparators().forEach { el ->
        when (el) {
            is CustomItem ->
                add(
                    swingActionItem(
                        title = el.title,
                        enabled = el.enabled,
                        shortcut = el.shortcut,
                        icon = el.icon,
                        tooltip = el.tooltip,
                        onClick = el.onClick
                    )
                )

            is CheckboxItem ->
                add(
                    swingCheckboxItem(
                        title = el.title,
                        checked = el.checked,
                        enabled = el.enabled,
                        shortcut = el.shortcut,
                        icon = el.icon,
                        tooltip = el.tooltip,
                        onToggle = el.onToggle
                    )
                )

            is Submenu ->
                add(
                    JMenu(el.title).apply {
                        isEnabled = el.enabled
                        el.icon?.toSwingIcon()?.let { icon = it }
                        addSwingElements(el.children)
                    }
                )

            is SectionHeader -> {
                val label = JLabel(el.title).apply {
                    font = font.deriveFont(Font.BOLD)
                    border = BorderFactory.createEmptyBorder(4, 6, 4, 6)
                    isOpaque = false
                }
                add(label)
            }

            is Separator -> addSeparator()

            is SystemItem.About -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is SystemItem.Settings -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is SystemItem.Services -> add(JMenuItem(el.title).apply { isEnabled = false })
            is SystemItem.Hide -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is SystemItem.HideOthers -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is SystemItem.ShowAll -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is SystemItem.Quit -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))

            is FileStd.New -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.Open -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.OpenRecent ->
                add(
                    JMenu(el.title).apply {
                        isEnabled = el.enabled
                        el.icon?.toSwingIcon()?.let { icon = it }
                        addSwingElements(el.children)
                    }
                )
            is FileStd.Close -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.CloseAll -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.Save -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.SaveAs -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.Duplicate -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.Rename -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.MoveTo -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.PageSetup -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.Print -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FileStd.ClearRecent -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))

            is EditStd.Undo -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Redo -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Cut -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Copy -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Paste -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.PasteAndMatchStyle -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Delete -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.SelectAll -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Find -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.FindAndReplace -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.FindNext -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.FindPrevious -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.UseSelectionForFind -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.JumpToSelection -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.ToggleSmartQuotes ->
                add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is EditStd.ToggleSmartDashes ->
                add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is EditStd.ToggleLinkDetection ->
                add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is EditStd.ToggleTextReplacement ->
                add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is EditStd.ToggleSpellingCorrection ->
                add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is EditStd.Uppercase -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Lowercase -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.Capitalize -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.StartSpeaking -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is EditStd.StopSpeaking -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))

            is FormatStd.ShowFonts -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.ShowColors -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.Bold -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.Italic -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.Underline -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.Bigger -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.Smaller -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.KerningStandard -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.KerningNone -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.KerningTighten -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.KerningLoosen -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.LigaturesNone -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.LigaturesStandard -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.LigaturesAll -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.BaselineStandard -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.RaiseBaseline -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.LowerBaseline -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.Superscript -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.Subscript -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is FormatStd.AlignLeft -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.AlignCenter -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.AlignRight -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is FormatStd.AlignJustified -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))

            is ViewStd.ShowToolbar -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is ViewStd.CustomizeToolbar -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is ViewStd.ToggleFullScreen -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is ViewStd.ToggleSidebar -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))
            is ViewStd.ToggleTabBar -> add(swingStdCheckboxItem(el.title, el.checked, el.enabled, icon = el.icon, onToggle = el.onToggle))

            is WindowStd.Close -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.Minimize -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.MinimizeAll -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.Zoom -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.BringAllToFront -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.ShowNextTab -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.ShowPreviousTab -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.MergeAllWindows -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
            is WindowStd.MoveTabToNewWindow -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))

            is HelpItem.AppHelp -> add(swingActionItem(el.title, el.enabled, icon = el.icon, onClick = el.onClick))
        }
    }
}

private fun swingActionItem(
    title: String,
    enabled: Boolean = true,
    shortcut: MenuShortcut? = null,
    icon: MenuIcon? = null,
    tooltip: String? = null,
    onClick: (() -> Unit)?,
): JMenuItem =
    JMenuItem(title).apply {
        isEnabled = enabled
        shortcut?.toKeyStroke()?.let { accelerator = it }
        icon?.toSwingIcon()?.let { this.icon = it }
        toolTipText = tooltip
        addActionListener { onClick?.invoke() }
    }

private fun swingCheckboxItem(
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    shortcut: MenuShortcut? = null,
    icon: MenuIcon? = null,
    tooltip: String? = null,
    onToggle: (Boolean) -> Unit,
): JCheckBoxMenuItem =
    JCheckBoxMenuItem(title, checked).apply {
        isEnabled = enabled
        shortcut?.toKeyStroke()?.let { accelerator = it }
        icon?.toSwingIcon()?.let { this.icon = it }
        toolTipText = tooltip
        addItemListener { onToggle(isSelected) }
    }

private fun swingStdCheckboxItem(
    title: String,
    checked: Boolean?,
    enabled: Boolean = true,
    icon: MenuIcon? = null,
    onToggle: ((Boolean) -> Unit)?,
): JCheckBoxMenuItem {
    val initial = checked ?: false
    return JCheckBoxMenuItem(title, initial).apply {
        isEnabled = enabled
        icon?.toSwingIcon()?.let { this.icon = it }
        addItemListener {
            val newValue = isSelected
            if (onToggle == null) {
                SwingUtilities.invokeLater { isSelected = initial }
            } else {
                onToggle(newValue)
            }
        }
    }
}

private val swingIconCache = WeakHashMap<MenuIcon, Icon?>()

private fun MenuIcon.toSwingIcon(): Icon? = synchronized(swingIconCache) {
    swingIconCache[this] ?: run {
        val icon = runCatching {
            when (this) {
                is MenuIcon.SFSymbol -> null
                is MenuIcon.File -> ImageIcon(path)
                is MenuIcon.Png -> {
                    val img = ImageIO.read(ByteArrayInputStream(bytes)) ?: return@runCatching null
                    ImageIcon(img)
                }
            }
        }.getOrNull()
        swingIconCache[this] = icon
        icon
    }
}

private fun MenuShortcut.toKeyStroke(): KeyStroke? {
    val keyCode = key.toAwtKeyCode() ?: return null
    val mods =
        (if (meta) InputEvent.META_DOWN_MASK else 0) or
                (if (ctrl) InputEvent.CTRL_DOWN_MASK else 0) or
                (if (alt) InputEvent.ALT_DOWN_MASK else 0) or
                (if (shift) InputEvent.SHIFT_DOWN_MASK else 0)
    return KeyStroke.getKeyStroke(keyCode, mods)
}

private fun Key.toAwtKeyCode(): Int? = when (this) {
    Key.Spacebar -> KeyEvent.VK_SPACE
    Key.Tab -> KeyEvent.VK_TAB
    Key.Enter, Key.NumPadEnter -> KeyEvent.VK_ENTER
    Key.Escape -> KeyEvent.VK_ESCAPE
    Key.Backspace -> KeyEvent.VK_BACK_SPACE
    Key.Delete -> KeyEvent.VK_DELETE

    Key.DirectionUp -> KeyEvent.VK_UP
    Key.DirectionDown -> KeyEvent.VK_DOWN
    Key.DirectionLeft -> KeyEvent.VK_LEFT
    Key.DirectionRight -> KeyEvent.VK_RIGHT

    Key.Home, Key.MoveHome -> KeyEvent.VK_HOME
    Key.MoveEnd -> KeyEvent.VK_END
    Key.PageUp -> KeyEvent.VK_PAGE_UP
    Key.PageDown -> KeyEvent.VK_PAGE_DOWN

    Key.F1 -> KeyEvent.VK_F1
    Key.F2 -> KeyEvent.VK_F2
    Key.F3 -> KeyEvent.VK_F3
    Key.F4 -> KeyEvent.VK_F4
    Key.F5 -> KeyEvent.VK_F5
    Key.F6 -> KeyEvent.VK_F6
    Key.F7 -> KeyEvent.VK_F7
    Key.F8 -> KeyEvent.VK_F8
    Key.F9 -> KeyEvent.VK_F9
    Key.F10 -> KeyEvent.VK_F10
    Key.F11 -> KeyEvent.VK_F11
    Key.F12 -> KeyEvent.VK_F12

    Key.Zero, Key.NumPad0 -> KeyEvent.VK_0
    Key.One, Key.NumPad1 -> KeyEvent.VK_1
    Key.Two, Key.NumPad2 -> KeyEvent.VK_2
    Key.Three, Key.NumPad3 -> KeyEvent.VK_3
    Key.Four, Key.NumPad4 -> KeyEvent.VK_4
    Key.Five, Key.NumPad5 -> KeyEvent.VK_5
    Key.Six, Key.NumPad6 -> KeyEvent.VK_6
    Key.Seven, Key.NumPad7 -> KeyEvent.VK_7
    Key.Eight, Key.NumPad8 -> KeyEvent.VK_8
    Key.Nine, Key.NumPad9 -> KeyEvent.VK_9

    Key.A -> KeyEvent.VK_A; Key.B -> KeyEvent.VK_B; Key.C -> KeyEvent.VK_C; Key.D -> KeyEvent.VK_D
    Key.E -> KeyEvent.VK_E; Key.F -> KeyEvent.VK_F; Key.G -> KeyEvent.VK_G; Key.H -> KeyEvent.VK_H
    Key.I -> KeyEvent.VK_I; Key.J -> KeyEvent.VK_J; Key.K -> KeyEvent.VK_K; Key.L -> KeyEvent.VK_L
    Key.M -> KeyEvent.VK_M; Key.N -> KeyEvent.VK_N; Key.O -> KeyEvent.VK_O; Key.P -> KeyEvent.VK_P
    Key.Q -> KeyEvent.VK_Q; Key.R -> KeyEvent.VK_R; Key.S -> KeyEvent.VK_S; Key.T -> KeyEvent.VK_T
    Key.U -> KeyEvent.VK_U; Key.V -> KeyEvent.VK_V; Key.W -> KeyEvent.VK_W; Key.X -> KeyEvent.VK_X
    Key.Y -> KeyEvent.VK_Y; Key.Z -> KeyEvent.VK_Z

    Key.Plus -> KeyEvent.VK_PLUS
    Key.NumPadAdd -> KeyEvent.VK_ADD
    Key.Minus -> KeyEvent.VK_MINUS
    Key.NumPadSubtract -> KeyEvent.VK_SUBTRACT
    Key.Multiply, Key.NumPadMultiply -> KeyEvent.VK_MULTIPLY
    Key.Equals, Key.NumPadEquals -> KeyEvent.VK_EQUALS

    Key.Comma, Key.NumPadComma -> KeyEvent.VK_COMMA
    Key.Period, Key.NumPadDot -> KeyEvent.VK_PERIOD
    Key.Slash, Key.NumPadDivide -> KeyEvent.VK_SLASH
    Key.Backslash -> KeyEvent.VK_BACK_SLASH
    Key.Semicolon -> KeyEvent.VK_SEMICOLON
    Key.Apostrophe -> KeyEvent.VK_QUOTE
    Key.Grave -> KeyEvent.VK_BACK_QUOTE
    Key.LeftBracket -> KeyEvent.VK_OPEN_BRACKET
    Key.RightBracket -> KeyEvent.VK_CLOSE_BRACKET
    Key.At -> KeyEvent.VK_AT
    Key.Pound -> KeyEvent.VK_NUMBER_SIGN

    else -> keyCode.takeIf { it != 0L }?.toInt()
}