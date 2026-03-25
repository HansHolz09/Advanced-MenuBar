package dev.hansholz.advancedmenubar

import composeadvancedmenubar.advanced_menubar.generated.resources.Res
import composeadvancedmenubar.advanced_menubar.generated.resources.about
import composeadvancedmenubar.advanced_menubar.generated.resources.align_center
import composeadvancedmenubar.advanced_menubar.generated.resources.align_justified
import composeadvancedmenubar.advanced_menubar.generated.resources.align_left
import composeadvancedmenubar.advanced_menubar.generated.resources.align_right
import composeadvancedmenubar.advanced_menubar.generated.resources.app_help
import composeadvancedmenubar.advanced_menubar.generated.resources.baseline
import composeadvancedmenubar.advanced_menubar.generated.resources.baseline_standard
import composeadvancedmenubar.advanced_menubar.generated.resources.bigger
import composeadvancedmenubar.advanced_menubar.generated.resources.bold
import composeadvancedmenubar.advanced_menubar.generated.resources.bring_all_to_front
import composeadvancedmenubar.advanced_menubar.generated.resources.capitalize
import composeadvancedmenubar.advanced_menubar.generated.resources.customize_toolbar
import composeadvancedmenubar.advanced_menubar.generated.resources.edit
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_copy
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_cut
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_delete
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_paste
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_redo
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_select_all
import composeadvancedmenubar.advanced_menubar.generated.resources.edit_undo
import composeadvancedmenubar.advanced_menubar.generated.resources.enter_full_screen
import composeadvancedmenubar.advanced_menubar.generated.resources.exit_full_screen
import composeadvancedmenubar.advanced_menubar.generated.resources.file
import composeadvancedmenubar.advanced_menubar.generated.resources.file_clear_recent
import composeadvancedmenubar.advanced_menubar.generated.resources.file_close
import composeadvancedmenubar.advanced_menubar.generated.resources.file_close_all
import composeadvancedmenubar.advanced_menubar.generated.resources.file_duplicate
import composeadvancedmenubar.advanced_menubar.generated.resources.file_move_to
import composeadvancedmenubar.advanced_menubar.generated.resources.file_new
import composeadvancedmenubar.advanced_menubar.generated.resources.file_open
import composeadvancedmenubar.advanced_menubar.generated.resources.file_open_recent
import composeadvancedmenubar.advanced_menubar.generated.resources.file_page_setup
import composeadvancedmenubar.advanced_menubar.generated.resources.file_print
import composeadvancedmenubar.advanced_menubar.generated.resources.file_rename
import composeadvancedmenubar.advanced_menubar.generated.resources.file_save
import composeadvancedmenubar.advanced_menubar.generated.resources.file_save_as
import composeadvancedmenubar.advanced_menubar.generated.resources.find
import composeadvancedmenubar.advanced_menubar.generated.resources.find_and_replace
import composeadvancedmenubar.advanced_menubar.generated.resources.find_dots
import composeadvancedmenubar.advanced_menubar.generated.resources.find_next
import composeadvancedmenubar.advanced_menubar.generated.resources.find_previous
import composeadvancedmenubar.advanced_menubar.generated.resources.font
import composeadvancedmenubar.advanced_menubar.generated.resources.format
import composeadvancedmenubar.advanced_menubar.generated.resources.help
import composeadvancedmenubar.advanced_menubar.generated.resources.hide
import composeadvancedmenubar.advanced_menubar.generated.resources.hide_others
import composeadvancedmenubar.advanced_menubar.generated.resources.hide_sidebar
import composeadvancedmenubar.advanced_menubar.generated.resources.hide_tab_bar
import composeadvancedmenubar.advanced_menubar.generated.resources.hide_toolbar
import composeadvancedmenubar.advanced_menubar.generated.resources.italic
import composeadvancedmenubar.advanced_menubar.generated.resources.jump_to_selection
import composeadvancedmenubar.advanced_menubar.generated.resources.kerning
import composeadvancedmenubar.advanced_menubar.generated.resources.kerning_loosen
import composeadvancedmenubar.advanced_menubar.generated.resources.kerning_none
import composeadvancedmenubar.advanced_menubar.generated.resources.kerning_standard
import composeadvancedmenubar.advanced_menubar.generated.resources.kerning_tighten
import composeadvancedmenubar.advanced_menubar.generated.resources.ligatures
import composeadvancedmenubar.advanced_menubar.generated.resources.ligatures_all
import composeadvancedmenubar.advanced_menubar.generated.resources.ligatures_none
import composeadvancedmenubar.advanced_menubar.generated.resources.ligatures_standard
import composeadvancedmenubar.advanced_menubar.generated.resources.lower_baseline
import composeadvancedmenubar.advanced_menubar.generated.resources.make_lower_case
import composeadvancedmenubar.advanced_menubar.generated.resources.make_upper_case
import composeadvancedmenubar.advanced_menubar.generated.resources.merge_all_windows
import composeadvancedmenubar.advanced_menubar.generated.resources.move_tab_to_new_window
import composeadvancedmenubar.advanced_menubar.generated.resources.paste_and_match_style
import composeadvancedmenubar.advanced_menubar.generated.resources.quit
import composeadvancedmenubar.advanced_menubar.generated.resources.raise_baseline
import composeadvancedmenubar.advanced_menubar.generated.resources.services
import composeadvancedmenubar.advanced_menubar.generated.resources.settings
import composeadvancedmenubar.advanced_menubar.generated.resources.show_all
import composeadvancedmenubar.advanced_menubar.generated.resources.show_colors
import composeadvancedmenubar.advanced_menubar.generated.resources.show_fonts
import composeadvancedmenubar.advanced_menubar.generated.resources.show_next_tab
import composeadvancedmenubar.advanced_menubar.generated.resources.show_previous_tab
import composeadvancedmenubar.advanced_menubar.generated.resources.show_sidebar
import composeadvancedmenubar.advanced_menubar.generated.resources.show_tab_bar
import composeadvancedmenubar.advanced_menubar.generated.resources.show_toolbar
import composeadvancedmenubar.advanced_menubar.generated.resources.smaller
import composeadvancedmenubar.advanced_menubar.generated.resources.speech
import composeadvancedmenubar.advanced_menubar.generated.resources.spelling_and_grammar
import composeadvancedmenubar.advanced_menubar.generated.resources.start_speaking
import composeadvancedmenubar.advanced_menubar.generated.resources.stop_speaking
import composeadvancedmenubar.advanced_menubar.generated.resources.subscript
import composeadvancedmenubar.advanced_menubar.generated.resources.substitutions
import composeadvancedmenubar.advanced_menubar.generated.resources.superscript
import composeadvancedmenubar.advanced_menubar.generated.resources.text
import composeadvancedmenubar.advanced_menubar.generated.resources.toggle_correct_spelling_automatically
import composeadvancedmenubar.advanced_menubar.generated.resources.toggle_smart_dashes
import composeadvancedmenubar.advanced_menubar.generated.resources.toggle_smart_links
import composeadvancedmenubar.advanced_menubar.generated.resources.toggle_smart_quotes
import composeadvancedmenubar.advanced_menubar.generated.resources.toggle_text_replacement
import composeadvancedmenubar.advanced_menubar.generated.resources.transformations
import composeadvancedmenubar.advanced_menubar.generated.resources.underline
import composeadvancedmenubar.advanced_menubar.generated.resources.use_selection_for_find
import composeadvancedmenubar.advanced_menubar.generated.resources.view
import composeadvancedmenubar.advanced_menubar.generated.resources.window
import composeadvancedmenubar.advanced_menubar.generated.resources.window_close
import composeadvancedmenubar.advanced_menubar.generated.resources.window_minimize
import composeadvancedmenubar.advanced_menubar.generated.resources.window_minimize_all
import composeadvancedmenubar.advanced_menubar.generated.resources.window_zoom
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.CheckboxItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.CustomItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.EditStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.FileStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.FormatStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.HelpItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.MenuElement
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.Submenu
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.SystemItem
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.TopMenu
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.ViewStd
import dev.hansholz.advancedmenubar.MacCocoaMenuBar.WindowStd
import org.jetbrains.compose.resources.StringResource

@MenuDsl
class AdvancedMenuBarScope(private val strings: List<Pair<StringResource, String>>) {
    internal val menus = mutableListOf<TopMenu>()
    private var hasApp = false
    private var hasFile = false
    private var hasEdit = false
    private var hasFormat = false
    private var hasView = false
    private var hasWindow = false
    private var hasHelp = false

    internal fun reset() {
        menus.clear()
        hasApp = false
        hasFile = false
        hasEdit = false
        hasFormat = false
        hasView = false
        hasWindow = false
        hasHelp = false
    }

    private fun getString(stringResource: StringResource): String =
        strings.find { it.first == stringResource }?.second ?: "STRING NOT FOUND"

    @MenuDsl
    class CompatibilityMenuScope(private val strings: List<Pair<StringResource, String>>) {
        internal val elements = mutableListOf<MenuElement>()

        private fun getString(stringResource: StringResource): String =
            strings.find { it.first == stringResource }?.second ?: "STRING NOT FOUND"

        /** MacOS only **/
        fun About(
            title: String = getString(Res.string.about),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.About(title, enabled, icon, onClick) }

        /** MacOS only **/
        fun Settings(
            title: String = getString(Res.string.settings),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.Settings(title, enabled, icon, onClick) }

        /** MacOS only **/
        fun Services(title: String = getString(Res.string.services)) {
            elements += SystemItem.Services(title)
        }

        /** MacOS only **/
        fun Hide(
            title: String = getString(Res.string.hide),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.Hide(title, enabled, icon, onClick) }

        /** MacOS only **/
        fun HideOthers(
            title: String = getString(Res.string.hide_others),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.HideOthers(title, enabled, icon, onClick) }

        /** MacOS only **/
        fun ShowAll(
            title: String = getString(Res.string.show_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.ShowAll(title, enabled, icon, onClick) }

        /** MacOS only **/
        fun Quit(
            title: String = getString(Res.string.quit),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) { elements += SystemItem.Quit(title, enabled, icon, onClick) }


        fun FileNew(
            title: String = getString(Res.string.file_new),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.New(title, enabled, icon, onClick) }

        fun FileOpen(
            title: String = getString(Res.string.file_open),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.Open(title, enabled, icon, onClick) }

        fun FileOpenRecent(
            title: String = getString(Res.string.file_open_recent),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            content: CompatibilityMenuScope.() -> Unit
        ) = ifVisible(visibility) {
            val s = CompatibilityMenuScope(strings)
            s.content()
            elements += FileStd.OpenRecent(title, s.elements.toList(), enabled, icon)
        }

        fun FileClearRecent(
            title: String = getString(Res.string.file_clear_recent),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.ClearRecent(title, enabled, icon, onClick) }

        fun FileClose(
            title: String = getString(Res.string.file_close),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += FileStd.Close(title, enabled, icon, onClick) }

        fun FileCloseAll(
            title: String = getString(Res.string.file_close_all),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += FileStd.CloseAll(title, enabled, icon, onClick) }

        fun FileSave(
            title: String = getString(Res.string.file_save),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.Save(title, enabled, icon, onClick) }

        fun FileSaveAs(
            title: String = getString(Res.string.file_save_as),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.SaveAs(title, enabled, icon, onClick) }

        fun FileDuplicate(
            title: String = getString(Res.string.file_duplicate),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.Duplicate(title, enabled, icon, onClick) }

        fun FileRename(
            title: String = getString(Res.string.file_rename),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.Rename(title, enabled, icon, onClick) }

        fun FileMoveTo(
            title: String = getString(Res.string.file_move_to),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.MoveTo(title, enabled, icon, onClick) }

        fun FilePageSetup(
            title: String = getString(Res.string.file_page_setup),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += FileStd.PageSetup(title, enabled, icon, onClick) }

        fun FilePrint(
            title: String = getString(Res.string.file_print),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) = ifVisible(visibility) { elements += FileStd.Print(title, enabled, icon, onClick) }


        fun Undo(
            title: String = getString(Res.string.edit_undo),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Undo(title, enabled, icon, onClick) }

        fun Redo(
            title: String = getString(Res.string.edit_redo),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Redo(title, enabled, icon, onClick) }

        fun Cut(
            title: String = getString(Res.string.edit_cut),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Cut(title, enabled, icon, onClick) }

        fun Copy(
            title: String = getString(Res.string.edit_copy),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Copy(title, enabled, icon, onClick) }

        fun Paste(
            title: String = getString(Res.string.edit_paste),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Paste(title, enabled, icon, onClick) }

        fun PasteAndMatchStyle(
            title: String = getString(Res.string.paste_and_match_style),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.PasteAndMatchStyle(title, enabled, icon, onClick) }

        fun Delete(
            title: String = getString(Res.string.edit_delete),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Delete(title, enabled, icon, onClick) }

        fun SelectAll(
            title: String = getString(Res.string.edit_select_all),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.SelectAll(title, enabled, icon, onClick) }


        fun FindMenu(
            title: String = getString(Res.string.find),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun Find(
            title: String = getString(Res.string.find_dots),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Find(title, enabled, icon, onClick) }

        fun FindAndReplace(
            title: String = getString(Res.string.find_and_replace),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.FindAndReplace(title, enabled, icon, onClick) }

        fun FindNext(
            title: String = getString(Res.string.find_next),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.FindNext(title, enabled, icon, onClick) }

        fun FindPrevious(
            title: String = getString(Res.string.find_previous),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.FindPrevious(title, enabled, icon, onClick) }

        fun UseSelectionForFind(
            title: String = getString(Res.string.use_selection_for_find),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.UseSelectionForFind(title, enabled, icon, onClick) }

        fun JumpToSelection(
            title: String = getString(Res.string.jump_to_selection),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.JumpToSelection(title, enabled, icon, onClick) }


        fun SpellingAndGrammarMenu(
            title: String = getString(Res.string.spelling_and_grammar),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun ToggleCorrectSpellingAutomatically(
            title: String = getString(Res.string.toggle_correct_spelling_automatically),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += EditStd.ToggleSpellingCorrection(title, enabled, icon, checked, onToggle) }


        fun SubstitutionsMenu(
            title: String = getString(Res.string.substitutions),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun ToggleSmartQuotes(
            title: String = getString(Res.string.toggle_smart_quotes),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += EditStd.ToggleSmartQuotes(title, enabled, icon, checked, onToggle) }

        fun ToggleSmartDashes(
            title: String = getString(Res.string.toggle_smart_dashes),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += EditStd.ToggleSmartDashes(title, enabled, icon, checked, onToggle) }

        fun ToggleSmartLinks(
            title: String = getString(Res.string.toggle_smart_links),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += EditStd.ToggleLinkDetection(title, enabled, icon, checked, onToggle) }

        fun ToggleTextReplacement(
            title: String = getString(Res.string.toggle_text_replacement),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: ((Boolean) -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.ToggleTextReplacement(title, enabled, icon, checked, onToggle) }


        fun TransformationsMenu(
            title: String = getString(Res.string.transformations),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun MakeUpperCase(
            title: String = getString(Res.string.make_upper_case),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Uppercase(title, enabled, icon, onClick) }

        fun MakeLowerCase(
            title: String = getString(Res.string.make_lower_case),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Lowercase(title, enabled, icon, onClick) }

        fun Capitalize(
            title: String = getString(Res.string.capitalize),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.Capitalize(title, enabled, icon, onClick) }


        fun SpeechMenu(
            title: String = getString(Res.string.speech),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun StartSpeaking(
            title: String = getString(Res.string.start_speaking),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.StartSpeaking(title, enabled, icon, onClick) }

        fun StopSpeaking(
            title: String = getString(Res.string.stop_speaking),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)
        ) = ifVisible(visibility) { elements += EditStd.StopSpeaking(title, enabled, icon, onClick) }


        fun ShowFonts(
            title: String = getString(Res.string.show_fonts),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += FormatStd.ShowFonts(title, enabled, icon, onClick) }

        fun ShowColors(
            title: String = getString(Res.string.show_colors),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += FormatStd.ShowColors(title, enabled, icon, onClick) }


        fun FontMenu(
            title: String = getString(Res.string.font),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun Bold(
            title: String = getString(Res.string.bold),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.Bold(title, enabled, icon, checked, onToggle) }

        fun Italic(
            title: String = getString(Res.string.italic),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.Italic(title, enabled, icon, checked, onToggle) }

        fun Underline(
            title: String = getString(Res.string.underline),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.Underline(title, enabled, icon, checked, onToggle) }

        fun Bigger(
            title: String = getString(Res.string.bigger),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.Bigger(title, enabled, icon, onClick) }

        fun Smaller(
            title: String = getString(Res.string.smaller),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.Smaller(title, enabled, icon, onClick) }


        fun KerningMenu(
            title: String = getString(Res.string.kerning),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun KerningStandard(
            title: String = getString(Res.string.kerning_standard),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.KerningStandard(title, enabled, icon, onClick) }

        fun KerningNone(
            title: String = getString(Res.string.kerning_none),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.KerningNone(title, enabled, icon, onClick) }

        fun KerningTighten(
            title: String = getString(Res.string.kerning_tighten),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.KerningTighten(title, enabled, icon, onClick) }

        fun KerningLoosen(
            title: String = getString(Res.string.kerning_loosen),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.KerningLoosen(title, enabled, icon, onClick) }


        fun LigaturesMenu(
            title: String = getString(Res.string.ligatures),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun LigaturesNone(
            title: String = getString(Res.string.ligatures_none),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.LigaturesNone(title, enabled, icon, onClick) }

        fun LigaturesStandard(
            title: String = getString(Res.string.ligatures_standard),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.LigaturesStandard(title, enabled, icon, onClick) }

        fun LigaturesAll(
            title: String = getString(Res.string.ligatures_all),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.LigaturesAll(title, enabled, icon, onClick) }


        fun BaselineMenu(
            title: String = getString(Res.string.baseline),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)


        fun BaselineStandard(
            title: String = getString(Res.string.baseline_standard),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.RaiseBaseline(title, enabled, icon, onClick) }

        fun RaiseBaseline(
            title: String = getString(Res.string.raise_baseline),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.RaiseBaseline(title, enabled, icon, onClick) }

        fun LowerBaseline(
            title: String = getString(Res.string.lower_baseline),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.LowerBaseline(title, enabled, icon, onClick) }

        fun Superscript(
            title: String = getString(Res.string.superscript),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.Superscript(title, enabled, icon, onClick) }

        fun Subscript(
            title: String = getString(Res.string.subscript),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += FormatStd.Subscript(title, enabled, icon, onClick) }


        fun TextMenu(
            title: String = getString(Res.string.text),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = Menu(title, visibility, enabled, icon, null, null, block)

        fun AlignLeft(
            title: String = getString(Res.string.align_left),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.AlignLeft(title, enabled, icon, checked, onToggle) }

        fun AlignCenter(
            title: String = getString(Res.string.align_center),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.AlignCenter(title, enabled, icon, checked, onToggle) }

        fun AlignRight(
            title: String = getString(Res.string.align_right),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.AlignRight(title, enabled, icon, checked, onToggle) }

        fun AlignJustified(
            title: String = getString(Res.string.align_justified),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += FormatStd.AlignJustified(title, enabled, icon, checked, onToggle) }


        fun ShowToolbar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_toolbar else Res.string.show_toolbar),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += ViewStd.ShowToolbar(title, enabled, icon, checked, onToggle) }

        fun CustomizeToolbar(
            title: String = getString(Res.string.customize_toolbar),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += ViewStd.CustomizeToolbar(title, enabled, icon, onClick) }

        fun ToggleFullScreen(
            state: Boolean,
            title: String = getString(if (state) Res.string.exit_full_screen else Res.string.enter_full_screen),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += ViewStd.ToggleFullScreen(title, enabled, icon, onClick) }

        fun ToggleSidebar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_sidebar else Res.string.show_sidebar),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += ViewStd.ToggleSidebar(title, enabled, icon, checked, onToggle) }

        fun ToggleTabBar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_tab_bar else Res.string.show_tab_bar),
            checked: Boolean = false,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += ViewStd.ToggleTabBar(title, enabled, icon, checked, onToggle) }


        fun Close(
            title: String = getString(Res.string.window_close),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += WindowStd.Close(title, enabled, icon, onClick) }

        fun Minimize(
            title: String = getString(Res.string.window_minimize),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += WindowStd.Minimize(title, enabled, icon, onClick) }

        fun MinimizeAll(
            title: String = getString(Res.string.window_minimize_all),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += WindowStd.MinimizeAll(title, enabled, icon, onClick) }

        fun Zoom(
            title: String = getString(Res.string.window_zoom),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += WindowStd.Zoom(title, enabled, icon, onClick) }

        fun BringAllToFront(
            title: String = getString(Res.string.bring_all_to_front),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += WindowStd.BringAllToFront(title, enabled, icon, onClick) }

        fun ShowNextTab(
            title: String = getString(Res.string.show_next_tab),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += WindowStd.ShowNextTab(title, enabled, icon, onClick) }

        fun ShowPreviousTab(
            title: String = getString(Res.string.show_previous_tab),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += WindowStd.ShowPreviousTab(title, enabled, icon, onClick) }

        fun MergeAllWindows(
            title: String = getString(Res.string.merge_all_windows),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += WindowStd.MergeAllWindows(title, enabled, icon, onClick) }

        fun MoveTabToNewWindow(
            title: String = getString(Res.string.move_tab_to_new_window),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?
        ) = ifVisible(visibility) { elements += WindowStd.MoveTabToNewWindow(title, enabled, icon, onClick) }


        fun AppHelp(
            title: String = getString(Res.string.app_help),
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null
        ) = ifVisible(visibility) { elements += HelpItem.AppHelp(title, enabled, icon, onClick) }


        fun Item(
            title: String,
            shortcut: MenuShortcut? = null,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            tooltip: String? = null,
            badge: String? = null,
            onClick: () -> Unit
        ) = ifVisible(visibility) { elements += CustomItem(title, shortcut, enabled, icon, subtitle, tooltip, badge, onClick) }

        fun Checkbox(
            title: String,
            checked: Boolean = false,
            shortcut: MenuShortcut? = null,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            tooltip: String? = null,
            badge: String? = null,
            onToggle: (Boolean) -> Unit
        ) = ifVisible(visibility) { elements += CheckboxItem(title, checked, shortcut, enabled, icon, subtitle, tooltip, badge, onToggle) }

        fun Separator(visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE) = ifVisible(visibility) { elements += MacCocoaMenuBar.Separator }

        fun Menu(
            title: String,
            visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            badge: String? = null,
            block: CompatibilityMenuScope.() -> Unit
        ) = ifVisible(visibility) {
            val s = CompatibilityMenuScope(strings)
            s.block()
            elements += Submenu(title, s.elements.toList(), enabled, icon, subtitle, badge)
        }

        private fun SectionHeader(title: String) {
            elements += MacCocoaMenuBar.SectionHeader(title)
        }

        fun CompatibilityMenuScope.Section(
            title: String,
            content: CompatibilityMenuScope.() -> Unit
        ) {
            Separator()
            SectionHeader(title)
            content()
            Separator()
        }
    }

    /** MacOS only **/
    fun MacApplicationMenu(content: CompatibilityMenuScope.() -> Unit) {
        if (hasApp) { println("[CompatibilityMenu] MacApplicationMenu already set – further call will be ignored."); return }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Application(s.elements.toList()); hasApp = true
    }

    fun FileMenu(
        title: String = getString(Res.string.file),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasFile) { println("[CompatibilityMenu] FileMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.File(title, s.elements.toList()); hasFile = true
    }

    fun EditMenu(
        title: String = getString(Res.string.edit),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasEdit) { println("[CompatibilityMenu] EditMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Edit(title, s.elements.toList()); hasEdit = true
    }

    fun FormatMenu(
        title: String = getString(Res.string.format),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasFormat) { println("[CompatibilityMenu] FormatMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Format(title, s.elements.toList()); hasFormat = true
    }

    fun ViewMenu(
        title: String = getString(Res.string.view),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasView) { println("[CompatibilityMenu] ViewMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.View(title, s.elements.toList()); hasView = true
    }

    fun WindowMenu(
        title: String = getString(Res.string.window),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        suppressAutoWindowList: Boolean = false,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasWindow) { println("[CompatibilityMenu] WindowMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Window(title, s.elements.toList(), suppressAutoWindowList); hasWindow = true
    }

    fun HelpMenu(
        title: String = getString(Res.string.help),
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        if (hasHelp) { println("[CompatibilityMenu] HelpMenu already set – further call will be ignored."); return@ifVisible }
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Help(title, s.elements.toList()); hasHelp = true
    }

    fun CustomMenu(
        title: String,
        visibility: MenuVisibility = MenuVisibility.ALWAYS_VISIBLE,
        content: CompatibilityMenuScope.() -> Unit
    ) = ifVisible(visibility) {
        val s = CompatibilityMenuScope(strings); s.content()
        menus += TopMenu.Custom(title, s.elements.toList())
    }
}