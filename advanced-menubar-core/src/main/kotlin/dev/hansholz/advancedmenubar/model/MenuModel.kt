package dev.hansholz.advancedmenubar

object MenuModel {
    sealed interface MenuElement

    data object Separator : MenuElement

    sealed class SystemItem : MenuElement {
        data class About(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()

        data class Settings(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()

        data class Services(
            val title: String,
        ) : SystemItem()

        data class Hide(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()

        data class HideOthers(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()

        data class ShowAll(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()

        data class Quit(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : SystemItem()
    }

    sealed class FileStd : MenuElement {
        data class New(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class Open(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class OpenRecent(
            val title: String,
            val children: List<MenuElement> = emptyList(),
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
        ) : FileStd()

        data class Close(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class CloseAll(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class Save(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class SaveAs(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class Duplicate(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class Rename(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class MoveTo(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class PageSetup(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class Print(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()

        data class ClearRecent(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FileStd()
    }

    sealed class EditStd : MenuElement {
        data class Undo(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Redo(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Cut(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Copy(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Paste(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class PasteAndMatchStyle(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Delete(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class SelectAll(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Find(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class FindAndReplace(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class FindNext(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class FindPrevious(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class UseSelectionForFind(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class JumpToSelection(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class ToggleSmartQuotes(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : EditStd()

        data class ToggleSmartDashes(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : EditStd()

        data class ToggleLinkDetection(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : EditStd()

        data class ToggleTextReplacement(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : EditStd()

        data class ToggleSpellingCorrection(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : EditStd()

        data class Uppercase(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Lowercase(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class Capitalize(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class StartSpeaking(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()

        data class StopSpeaking(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : EditStd()
    }

    sealed class FormatStd : MenuElement {
        data class ShowFonts(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class ShowColors(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class Bold(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class Italic(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class Underline(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class Bigger(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class Smaller(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class KerningStandard(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class KerningNone(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class KerningTighten(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class KerningLoosen(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class LigaturesNone(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class LigaturesStandard(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class LigaturesAll(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class BaselineStandard(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class RaiseBaseline(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class LowerBaseline(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class Superscript(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class Subscript(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : FormatStd()

        data class AlignLeft(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class AlignCenter(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class AlignRight(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()

        data class AlignJustified(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : FormatStd()
    }

    sealed class ViewStd : MenuElement {
        data class ShowToolbar(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: ((Boolean) -> Unit)? = null,
        ) : ViewStd()

        data class CustomizeToolbar(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : ViewStd()

        data class ToggleFullScreen(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : ViewStd()

        data class ToggleSidebar(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : ViewStd()

        data class ToggleTabBar(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val checked: Boolean? = null,
            val onToggle: (
                (Boolean) -> Unit
            )? = null,
        ) : ViewStd()
    }

    sealed class WindowStd : MenuElement {
        data class Close(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class Minimize(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class MinimizeAll(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class Zoom(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class BringAllToFront(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class ShowNextTab(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class ShowPreviousTab(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class MergeAllWindows(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()

        data class MoveTabToNewWindow(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : WindowStd()
    }

    sealed class HelpItem : MenuElement {
        data class AppHelp(
            val title: String,
            val enabled: Boolean = true,
            val icon: MenuIcon? = null,
            val onClick: (() -> Unit)? = null,
        ) : HelpItem()
    }

    data class CustomItem(
        val title: String,
        val shortcut: MenuShortcut? = null,
        val enabled: Boolean = true,
        val icon: MenuIcon? = null,
        val subtitle: String? = null,
        val tooltip: String? = null,
        val badge: String? = null,
        val onClick: () -> Unit,
    ) : MenuElement

    data class CheckboxItem(
        val title: String,
        val checked: Boolean = false,
        val shortcut: MenuShortcut? = null,
        val enabled: Boolean = true,
        val icon: MenuIcon? = null,
        val subtitle: String? = null,
        val tooltip: String? = null,
        val badge: String? = null,
        val onToggle: (Boolean) -> Unit,
    ) : MenuElement

    data class Submenu(
        val title: String,
        val children: List<MenuElement>,
        val enabled: Boolean = true,
        val icon: MenuIcon? = null,
        val subtitle: String? = null,
        val badge: String? = null,
    ) : MenuElement

    data class SectionHeader(
        val title: String,
    ) : MenuElement

    sealed class TopMenu {
        data class Application(
            val elements: List<MenuElement>,
        ) : TopMenu()

        data class File(
            val title: String,
            val elements: List<MenuElement>,
        ) : TopMenu()

        data class Edit(
            val title: String,
            val elements: List<MenuElement>,
            val suppressAutomaticItems: Boolean,
        ) : TopMenu()

        data class Format(
            val title: String,
            val elements: List<MenuElement>,
        ) : TopMenu()

        data class View(
            val title: String,
            val elements: List<MenuElement>,
        ) : TopMenu()

        data class Window(
            val title: String,
            val elements: List<MenuElement>,
            val suppressAutoWindowList: Boolean,
        ) : TopMenu()

        data class Help(
            val title: String,
            val elements: List<MenuElement>,
        ) : TopMenu()

        data class Custom(
            val title: String,
            val elements: List<MenuElement>,
        ) : TopMenu()
    }
}
