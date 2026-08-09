@file:OptIn(InternalAdvancedMenuBarApi::class)
@file:Suppress("FunctionName")

package dev.hansholz.advancedmenubar

import composeadvancedmenubar.generated.resources.Res
import composeadvancedmenubar.generated.resources.about
import composeadvancedmenubar.generated.resources.align_center
import composeadvancedmenubar.generated.resources.align_justified
import composeadvancedmenubar.generated.resources.align_left
import composeadvancedmenubar.generated.resources.align_right
import composeadvancedmenubar.generated.resources.app_help
import composeadvancedmenubar.generated.resources.baseline
import composeadvancedmenubar.generated.resources.baseline_standard
import composeadvancedmenubar.generated.resources.bigger
import composeadvancedmenubar.generated.resources.bold
import composeadvancedmenubar.generated.resources.bring_all_to_front
import composeadvancedmenubar.generated.resources.capitalize
import composeadvancedmenubar.generated.resources.customize_toolbar
import composeadvancedmenubar.generated.resources.edit
import composeadvancedmenubar.generated.resources.edit_copy
import composeadvancedmenubar.generated.resources.edit_cut
import composeadvancedmenubar.generated.resources.edit_delete
import composeadvancedmenubar.generated.resources.edit_paste
import composeadvancedmenubar.generated.resources.edit_redo
import composeadvancedmenubar.generated.resources.edit_select_all
import composeadvancedmenubar.generated.resources.edit_undo
import composeadvancedmenubar.generated.resources.enter_full_screen
import composeadvancedmenubar.generated.resources.exit_full_screen
import composeadvancedmenubar.generated.resources.file
import composeadvancedmenubar.generated.resources.file_clear_recent
import composeadvancedmenubar.generated.resources.file_close
import composeadvancedmenubar.generated.resources.file_close_all
import composeadvancedmenubar.generated.resources.file_duplicate
import composeadvancedmenubar.generated.resources.file_move_to
import composeadvancedmenubar.generated.resources.file_new
import composeadvancedmenubar.generated.resources.file_open
import composeadvancedmenubar.generated.resources.file_open_recent
import composeadvancedmenubar.generated.resources.file_page_setup
import composeadvancedmenubar.generated.resources.file_print
import composeadvancedmenubar.generated.resources.file_rename
import composeadvancedmenubar.generated.resources.file_save
import composeadvancedmenubar.generated.resources.file_save_as
import composeadvancedmenubar.generated.resources.find
import composeadvancedmenubar.generated.resources.find_and_replace
import composeadvancedmenubar.generated.resources.find_dots
import composeadvancedmenubar.generated.resources.find_next
import composeadvancedmenubar.generated.resources.find_previous
import composeadvancedmenubar.generated.resources.font
import composeadvancedmenubar.generated.resources.format
import composeadvancedmenubar.generated.resources.help
import composeadvancedmenubar.generated.resources.hide
import composeadvancedmenubar.generated.resources.hide_others
import composeadvancedmenubar.generated.resources.hide_sidebar
import composeadvancedmenubar.generated.resources.hide_tab_bar
import composeadvancedmenubar.generated.resources.hide_toolbar
import composeadvancedmenubar.generated.resources.italic
import composeadvancedmenubar.generated.resources.jump_to_selection
import composeadvancedmenubar.generated.resources.kerning
import composeadvancedmenubar.generated.resources.kerning_loosen
import composeadvancedmenubar.generated.resources.kerning_none
import composeadvancedmenubar.generated.resources.kerning_standard
import composeadvancedmenubar.generated.resources.kerning_tighten
import composeadvancedmenubar.generated.resources.ligatures
import composeadvancedmenubar.generated.resources.ligatures_all
import composeadvancedmenubar.generated.resources.ligatures_none
import composeadvancedmenubar.generated.resources.ligatures_standard
import composeadvancedmenubar.generated.resources.lower_baseline
import composeadvancedmenubar.generated.resources.make_lower_case
import composeadvancedmenubar.generated.resources.make_upper_case
import composeadvancedmenubar.generated.resources.merge_all_windows
import composeadvancedmenubar.generated.resources.move_tab_to_new_window
import composeadvancedmenubar.generated.resources.paste_and_match_style
import composeadvancedmenubar.generated.resources.quit
import composeadvancedmenubar.generated.resources.raise_baseline
import composeadvancedmenubar.generated.resources.services
import composeadvancedmenubar.generated.resources.settings
import composeadvancedmenubar.generated.resources.show_all
import composeadvancedmenubar.generated.resources.show_colors
import composeadvancedmenubar.generated.resources.show_fonts
import composeadvancedmenubar.generated.resources.show_next_tab
import composeadvancedmenubar.generated.resources.show_previous_tab
import composeadvancedmenubar.generated.resources.show_sidebar
import composeadvancedmenubar.generated.resources.show_tab_bar
import composeadvancedmenubar.generated.resources.show_toolbar
import composeadvancedmenubar.generated.resources.smaller
import composeadvancedmenubar.generated.resources.speech
import composeadvancedmenubar.generated.resources.spelling_and_grammar
import composeadvancedmenubar.generated.resources.start_speaking
import composeadvancedmenubar.generated.resources.stop_speaking
import composeadvancedmenubar.generated.resources.subscript
import composeadvancedmenubar.generated.resources.substitutions
import composeadvancedmenubar.generated.resources.superscript
import composeadvancedmenubar.generated.resources.text
import composeadvancedmenubar.generated.resources.toggle_correct_spelling_automatically
import composeadvancedmenubar.generated.resources.toggle_smart_dashes
import composeadvancedmenubar.generated.resources.toggle_smart_links
import composeadvancedmenubar.generated.resources.toggle_smart_quotes
import composeadvancedmenubar.generated.resources.toggle_text_replacement
import composeadvancedmenubar.generated.resources.transformations
import composeadvancedmenubar.generated.resources.underline
import composeadvancedmenubar.generated.resources.use_selection_for_find
import composeadvancedmenubar.generated.resources.view
import composeadvancedmenubar.generated.resources.window
import composeadvancedmenubar.generated.resources.window_close
import composeadvancedmenubar.generated.resources.window_minimize
import composeadvancedmenubar.generated.resources.window_minimize_all
import composeadvancedmenubar.generated.resources.window_zoom
import dev.hansholz.advancedmenubar.MenuModel.CheckboxItem
import dev.hansholz.advancedmenubar.MenuModel.CustomItem
import dev.hansholz.advancedmenubar.MenuModel.EditStd
import dev.hansholz.advancedmenubar.MenuModel.FileStd
import dev.hansholz.advancedmenubar.MenuModel.FormatStd
import dev.hansholz.advancedmenubar.MenuModel.HelpItem
import dev.hansholz.advancedmenubar.MenuModel.MenuElement
import dev.hansholz.advancedmenubar.MenuModel.Submenu
import dev.hansholz.advancedmenubar.MenuModel.SystemItem
import dev.hansholz.advancedmenubar.MenuModel.TopMenu
import dev.hansholz.advancedmenubar.MenuModel.ViewStd
import dev.hansholz.advancedmenubar.MenuModel.WindowStd
import org.jetbrains.compose.resources.StringResource

/**
 * Receiver for the top-level Advanced MenuBar DSL.
 *
 * The application, File, Edit, Format, View, Window, and Help menus may each be declared once;
 * [CustomMenu] may be declared any number of times. Use ordinary Kotlin conditions to include or
 * remove menus in response to state.
 */
@MenuDsl
class MenuBarScope internal constructor(
    private val strings: List<Pair<StringResource, String>>,
) {
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

    /**
     * Receiver for items inside a top-level menu or submenu.
     *
     * Standard functions such as [Undo], [FileSave], and [ToggleFullScreen] carry native macOS
     * selectors and localized labels. Supplying a callback overrides native behavior. Swing has no
     * AppKit responder chain, so standard entries without callbacks are rendered disabled there.
     * Custom [Item] and [Checkbox] entries always dispatch their supplied callbacks.
     */
    @MenuDsl
    class MenuScope internal constructor(
        private val strings: List<Pair<StringResource, String>>,
    ) {
        internal val elements = mutableListOf<MenuElement>()

        private fun getString(stringResource: StringResource): String =
            strings.find { it.first == stringResource }?.second ?: "STRING NOT FOUND"

        /** Adds the standard About item, or invokes [onClick] instead of AppKit's About panel. */
        fun About(
            title: String = getString(Res.string.about),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.About(title, enabled, icon, onClick)
        }

        /** Adds the standard application settings/preferences item. */
        fun Settings(
            title: String = getString(Res.string.settings),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.Settings(title, enabled, icon, onClick)
        }

        /** Adds the AppKit-managed Services submenu. */
        fun Services(title: String = getString(Res.string.services)) {
            elements += SystemItem.Services(title)
        }

        /** Adds the standard action that hides the current application. */
        fun Hide(
            title: String = getString(Res.string.hide),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.Hide(title, enabled, icon, onClick)
        }

        /** Adds the standard action that hides other applications. */
        fun HideOthers(
            title: String = getString(Res.string.hide_others),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.HideOthers(title, enabled, icon, onClick)
        }

        /** Adds the standard action that reveals hidden applications. */
        fun ShowAll(
            title: String = getString(Res.string.show_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.ShowAll(title, enabled, icon, onClick)
        }

        /** Adds the standard action that terminates the application. */
        fun Quit(
            title: String = getString(Res.string.quit),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += SystemItem.Quit(title, enabled, icon, onClick)
        }

        /** Adds the standard New document action. */
        fun FileNew(
            title: String = getString(Res.string.file_new),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.New(title, enabled, icon, onClick)
        }

        /** Adds the standard Open document action. */
        fun FileOpen(
            title: String = getString(Res.string.file_open),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.Open(title, enabled, icon, onClick)
        }

        /** Adds the standard Open Recent submenu and builds its children with [content]. */
        fun FileOpenRecent(
            title: String = getString(Res.string.file_open_recent),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            content: MenuScope.() -> Unit,
        ) {
            val s = MenuScope(strings)
            s.content()
            elements += FileStd.OpenRecent(title, s.elements.toList(), enabled, icon)
        }

        /** Adds the standard Clear Recent action. */
        fun FileClearRecent(
            title: String = getString(Res.string.file_clear_recent),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.ClearRecent(title, enabled, icon, onClick)
        }

        /** Adds the standard Close document action. */
        fun FileClose(
            title: String = getString(Res.string.file_close),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += FileStd.Close(title, enabled, icon, onClick)
        }

        /** Adds the standard Close All documents action. */
        fun FileCloseAll(
            title: String = getString(Res.string.file_close_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += FileStd.CloseAll(title, enabled, icon, onClick)
        }

        /** Adds the standard Save action. */
        fun FileSave(
            title: String = getString(Res.string.file_save),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.Save(title, enabled, icon, onClick)
        }

        /** Adds the standard Save As action. */
        fun FileSaveAs(
            title: String = getString(Res.string.file_save_as),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.SaveAs(title, enabled, icon, onClick)
        }

        /** Adds the standard Duplicate document action. */
        fun FileDuplicate(
            title: String = getString(Res.string.file_duplicate),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.Duplicate(title, enabled, icon, onClick)
        }

        /** Adds the standard Rename document action. */
        fun FileRename(
            title: String = getString(Res.string.file_rename),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.Rename(title, enabled, icon, onClick)
        }

        /** Adds the standard Move To action. */
        fun FileMoveTo(
            title: String = getString(Res.string.file_move_to),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.MoveTo(title, enabled, icon, onClick)
        }

        /** Adds the standard Page Setup action. */
        fun FilePageSetup(
            title: String = getString(Res.string.file_page_setup),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += FileStd.PageSetup(title, enabled, icon, onClick)
        }

        /** Adds the standard Print action. */
        fun FilePrint(
            title: String = getString(Res.string.file_print),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FileStd.Print(title, enabled, icon, onClick)
        }

        /** Adds Undo; callback-free native items target the focused Compose editor. */
        fun Undo(
            title: String = getString(Res.string.edit_undo),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Undo(title, enabled, icon, onClick)
        }

        /** Adds Redo; callback-free native items target the focused Compose editor. */
        fun Redo(
            title: String = getString(Res.string.edit_redo),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Redo(title, enabled, icon, onClick)
        }

        /** Adds Cut; callback-free native items target the focused Compose editor. */
        fun Cut(
            title: String = getString(Res.string.edit_cut),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Cut(title, enabled, icon, onClick)
        }

        /** Adds Copy; callback-free native items target the focused Compose editor. */
        fun Copy(
            title: String = getString(Res.string.edit_copy),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Copy(title, enabled, icon, onClick)
        }

        /** Adds Paste; callback-free native items target the focused Compose editor. */
        fun Paste(
            title: String = getString(Res.string.edit_paste),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Paste(title, enabled, icon, onClick)
        }

        /** Adds Paste and Match Style. */
        fun PasteAndMatchStyle(
            title: String = getString(Res.string.paste_and_match_style),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.PasteAndMatchStyle(title, enabled, icon, onClick)
        }

        /** Adds Delete; callback-free native items target the focused Compose editor. */
        fun Delete(
            title: String = getString(Res.string.edit_delete),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Delete(title, enabled, icon, onClick)
        }

        /** Adds Select All; callback-free native items target the focused Compose editor. */
        fun SelectAll(
            title: String = getString(Res.string.edit_select_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.SelectAll(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Find submenu. */
        fun FindMenu(
            title: String = getString(Res.string.find),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds the standard Find action. */
        fun Find(
            title: String = getString(Res.string.find_dots),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.Find(title, enabled, icon, onClick)
        }

        /** Adds the standard Find and Replace action. */
        fun FindAndReplace(
            title: String = getString(Res.string.find_and_replace),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.FindAndReplace(title, enabled, icon, onClick)
        }

        /** Adds the standard Find Next action. */
        fun FindNext(
            title: String = getString(Res.string.find_next),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.FindNext(title, enabled, icon, onClick)
        }

        /** Adds the standard Find Previous action. */
        fun FindPrevious(
            title: String = getString(Res.string.find_previous),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.FindPrevious(title, enabled, icon, onClick)
        }

        /** Adds Use Selection for Find. */
        fun UseSelectionForFind(
            title: String = getString(Res.string.use_selection_for_find),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.UseSelectionForFind(title, enabled, icon, onClick)
        }

        /** Adds Jump to Selection. */
        fun JumpToSelection(
            title: String = getString(Res.string.jump_to_selection),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += EditStd.JumpToSelection(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Spelling and Grammar submenu. */
        fun SpellingAndGrammarMenu(
            title: String = getString(Res.string.spelling_and_grammar),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds a controlled Correct Spelling Automatically toggle. */
        fun ToggleCorrectSpellingAutomatically(
            title: String = getString(Res.string.toggle_correct_spelling_automatically),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += EditStd.ToggleSpellingCorrection(title, enabled, icon, checked, onToggle)
        }

        /** Adds a conventionally titled Substitutions submenu. */
        fun SubstitutionsMenu(
            title: String = getString(Res.string.substitutions),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds a controlled Smart Quotes toggle. */
        fun ToggleSmartQuotes(
            title: String = getString(Res.string.toggle_smart_quotes),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += EditStd.ToggleSmartQuotes(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Smart Dashes toggle. */
        fun ToggleSmartDashes(
            title: String = getString(Res.string.toggle_smart_dashes),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += EditStd.ToggleSmartDashes(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Smart Links toggle. */
        fun ToggleSmartLinks(
            title: String = getString(Res.string.toggle_smart_links),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += EditStd.ToggleLinkDetection(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Text Replacement toggle. */
        fun ToggleTextReplacement(
            title: String = getString(Res.string.toggle_text_replacement),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: ((Boolean) -> Unit),
        ) {
            elements += EditStd.ToggleTextReplacement(title, enabled, icon, checked, onToggle)
        }

        /** Adds a conventionally titled Transformations submenu. */
        fun TransformationsMenu(
            title: String = getString(Res.string.transformations),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds the standard uppercase transformation. */
        fun MakeUpperCase(
            title: String = getString(Res.string.make_upper_case),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit),
        ) {
            elements += EditStd.Uppercase(title, enabled, icon, onClick)
        }

        /** Adds the standard lowercase transformation. */
        fun MakeLowerCase(
            title: String = getString(Res.string.make_lower_case),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit),
        ) {
            elements += EditStd.Lowercase(title, enabled, icon, onClick)
        }

        /** Adds the standard capitalization transformation. */
        fun Capitalize(
            title: String = getString(Res.string.capitalize),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit),
        ) {
            elements += EditStd.Capitalize(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Speech submenu. */
        fun SpeechMenu(
            title: String = getString(Res.string.speech),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds Start Speaking. */
        fun StartSpeaking(
            title: String = getString(Res.string.start_speaking),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit),
        ) {
            elements += EditStd.StartSpeaking(title, enabled, icon, onClick)
        }

        /** Adds Stop Speaking. */
        fun StopSpeaking(
            title: String = getString(Res.string.stop_speaking),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit),
        ) {
            elements += EditStd.StopSpeaking(title, enabled, icon, onClick)
        }

        /** Adds the standard action that opens the font panel. */
        fun ShowFonts(
            title: String = getString(Res.string.show_fonts),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += FormatStd.ShowFonts(title, enabled, icon, onClick)
        }

        /** Adds the standard action that opens the color panel. */
        fun ShowColors(
            title: String = getString(Res.string.show_colors),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += FormatStd.ShowColors(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Font submenu. */
        fun FontMenu(
            title: String = getString(Res.string.font),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds a controlled Bold formatting toggle. */
        fun Bold(
            title: String = getString(Res.string.bold),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.Bold(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Italic formatting toggle. */
        fun Italic(
            title: String = getString(Res.string.italic),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.Italic(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Underline formatting toggle. */
        fun Underline(
            title: String = getString(Res.string.underline),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.Underline(title, enabled, icon, checked, onToggle)
        }

        /** Adds the standard Make Text Bigger action. */
        fun Bigger(
            title: String = getString(Res.string.bigger),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.Bigger(title, enabled, icon, onClick)
        }

        /** Adds the standard Make Text Smaller action. */
        fun Smaller(
            title: String = getString(Res.string.smaller),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.Smaller(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Kerning submenu. */
        fun KerningMenu(
            title: String = getString(Res.string.kerning),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds Use Standard Kerning. */
        fun KerningStandard(
            title: String = getString(Res.string.kerning_standard),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.KerningStandard(title, enabled, icon, onClick)
        }

        /** Adds Turn Off Kerning. */
        fun KerningNone(
            title: String = getString(Res.string.kerning_none),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.KerningNone(title, enabled, icon, onClick)
        }

        /** Adds Tighten Kerning. */
        fun KerningTighten(
            title: String = getString(Res.string.kerning_tighten),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.KerningTighten(title, enabled, icon, onClick)
        }

        /** Adds Loosen Kerning. */
        fun KerningLoosen(
            title: String = getString(Res.string.kerning_loosen),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.KerningLoosen(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Ligatures submenu. */
        fun LigaturesMenu(
            title: String = getString(Res.string.ligatures),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds Turn Off Ligatures. */
        fun LigaturesNone(
            title: String = getString(Res.string.ligatures_none),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.LigaturesNone(title, enabled, icon, onClick)
        }

        /** Adds Use Standard Ligatures. */
        fun LigaturesStandard(
            title: String = getString(Res.string.ligatures_standard),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.LigaturesStandard(title, enabled, icon, onClick)
        }

        /** Adds Use All Ligatures. */
        fun LigaturesAll(
            title: String = getString(Res.string.ligatures_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.LigaturesAll(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Baseline submenu. */
        fun BaselineMenu(
            title: String = getString(Res.string.baseline),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds the standard baseline action. */
        fun BaselineStandard(
            title: String = getString(Res.string.baseline_standard),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.BaselineStandard(title, enabled, icon, onClick)
        }

        /** Adds Raise Baseline. */
        fun RaiseBaseline(
            title: String = getString(Res.string.raise_baseline),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.RaiseBaseline(title, enabled, icon, onClick)
        }

        /** Adds Lower Baseline. */
        fun LowerBaseline(
            title: String = getString(Res.string.lower_baseline),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.LowerBaseline(title, enabled, icon, onClick)
        }

        /** Adds Superscript. */
        fun Superscript(
            title: String = getString(Res.string.superscript),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.Superscript(title, enabled, icon, onClick)
        }

        /** Adds Subscript. */
        fun Subscript(
            title: String = getString(Res.string.subscript),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += FormatStd.Subscript(title, enabled, icon, onClick)
        }

        /** Adds a conventionally titled Text/alignment submenu. */
        fun TextMenu(
            title: String = getString(Res.string.text),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            block: MenuScope.() -> Unit,
        ) = Menu(title, enabled, icon, null, null, block)

        /** Adds a controlled Align Left item. */
        fun AlignLeft(
            title: String = getString(Res.string.align_left),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.AlignLeft(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Align Center item. */
        fun AlignCenter(
            title: String = getString(Res.string.align_center),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.AlignCenter(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Align Right item. */
        fun AlignRight(
            title: String = getString(Res.string.align_right),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.AlignRight(title, enabled, icon, checked, onToggle)
        }

        /** Adds a controlled Justified alignment item. */
        fun AlignJustified(
            title: String = getString(Res.string.align_justified),
            checked: Boolean = false,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += FormatStd.AlignJustified(title, enabled, icon, checked, onToggle)
        }

        /** Adds Show/Hide Toolbar; [state] controls both the localized title and default checkmark. */
        fun ShowToolbar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_toolbar else Res.string.show_toolbar),
            checked: Boolean = state,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += ViewStd.ShowToolbar(title, enabled, icon, checked, onToggle)
        }

        /** Adds Customize Toolbar. */
        fun CustomizeToolbar(
            title: String = getString(Res.string.customize_toolbar),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += ViewStd.CustomizeToolbar(title, enabled, icon, onClick)
        }

        /** Adds Enter/Exit Full Screen with a title derived from [state]. */
        fun ToggleFullScreen(
            state: Boolean,
            title: String = getString(if (state) Res.string.exit_full_screen else Res.string.enter_full_screen),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += ViewStd.ToggleFullScreen(title, enabled, icon, onClick)
        }

        /** Adds Show/Hide Sidebar; [state] controls both the localized title and default checkmark. */
        fun ToggleSidebar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_sidebar else Res.string.show_sidebar),
            checked: Boolean = state,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += ViewStd.ToggleSidebar(title, enabled, icon, checked, onToggle)
        }

        /** Adds Show/Hide Tab Bar; [state] controls both the localized title and default checkmark. */
        fun ToggleTabBar(
            state: Boolean,
            title: String = getString(if (state) Res.string.hide_tab_bar else Res.string.show_tab_bar),
            checked: Boolean = state,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += ViewStd.ToggleTabBar(title, enabled, icon, checked, onToggle)
        }

        /** Adds the standard Close Window action. */
        fun Close(
            title: String = getString(Res.string.window_close),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += WindowStd.Close(title, enabled, icon, onClick)
        }

        /** Adds the standard Minimize Window action. */
        fun Minimize(
            title: String = getString(Res.string.window_minimize),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += WindowStd.Minimize(title, enabled, icon, onClick)
        }

        /** Adds the standard Minimize All action. */
        fun MinimizeAll(
            title: String = getString(Res.string.window_minimize_all),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += WindowStd.MinimizeAll(title, enabled, icon, onClick)
        }

        /** Adds the standard Zoom Window action. */
        fun Zoom(
            title: String = getString(Res.string.window_zoom),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += WindowStd.Zoom(title, enabled, icon, onClick)
        }

        /** Adds Bring All to Front. */
        fun BringAllToFront(
            title: String = getString(Res.string.bring_all_to_front),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += WindowStd.BringAllToFront(title, enabled, icon, onClick)
        }

        /** Adds Show Next Tab. */
        fun ShowNextTab(
            title: String = getString(Res.string.show_next_tab),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += WindowStd.ShowNextTab(title, enabled, icon, onClick)
        }

        /** Adds Show Previous Tab. */
        fun ShowPreviousTab(
            title: String = getString(Res.string.show_previous_tab),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += WindowStd.ShowPreviousTab(title, enabled, icon, onClick)
        }

        /** Adds Merge All Windows. */
        fun MergeAllWindows(
            title: String = getString(Res.string.merge_all_windows),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += WindowStd.MergeAllWindows(title, enabled, icon, onClick)
        }

        /** Adds Move Tab to New Window. */
        fun MoveTabToNewWindow(
            title: String = getString(Res.string.move_tab_to_new_window),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)?,
        ) {
            elements += WindowStd.MoveTabToNewWindow(title, enabled, icon, onClick)
        }

        /** Adds the standard application Help action. */
        fun AppHelp(
            title: String = getString(Res.string.app_help),
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            onClick: (() -> Unit)? = null,
        ) {
            elements += HelpItem.AppHelp(title, enabled, icon, onClick)
        }

        /**
         * Adds an application-defined action item.
         *
         * Subtitles and badges are AppKit-only. Swing renders the title, shortcut, supported icon,
         * enabled state, and tooltip, and deliberately omits unsupported presentation.
         */
        fun Item(
            title: String,
            shortcut: MenuShortcut? = null,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            tooltip: String? = null,
            badge: String? = null,
            onClick: () -> Unit,
        ) {
            elements += CustomItem(title, shortcut, enabled, icon, subtitle, tooltip, badge, onClick)
        }

        /**
         * Adds an application-controlled checked item.
         *
         * [onToggle] receives the requested new value; update [checked] state to reflect it on the
         * next composition.
         */
        fun Checkbox(
            title: String,
            checked: Boolean = false,
            shortcut: MenuShortcut? = null,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            tooltip: String? = null,
            badge: String? = null,
            onToggle: (Boolean) -> Unit,
        ) {
            elements += CheckboxItem(title, checked, shortcut, enabled, icon, subtitle, tooltip, badge, onToggle)
        }

        /** Adds a separator. AppKit handles native separator presentation and normalization. */
        fun Separator() {
            elements += MenuModel.Separator
        }

        /**
         * Adds a nested submenu.
         *
         * [subtitle] and [badge] are available in the native macOS renderer and intentionally
         * omitted by Swing, whose standard menu API has no equivalent presentation.
         */
        fun Menu(
            title: String,
            enabled: Boolean = true,
            icon: MenuIcon? = null,
            subtitle: String? = null,
            badge: String? = null,
            block: MenuScope.() -> Unit,
        ) {
            val s = MenuScope(strings)
            s.block()
            elements += Submenu(title, s.elements.toList(), enabled, icon, subtitle, badge)
        }

        private fun SectionHeader(title: String) {
            elements += MenuModel.SectionHeader(title)
        }

        /** Adds a native section header and surrounds the section with separators. */
        fun MenuScope.Section(
            title: String,
            content: MenuScope.() -> Unit,
        ) {
            Separator()
            SectionHeader(title)
            content()
            Separator()
        }
    }

    /** Declares the process application menu. Swing omits this macOS-specific top-level menu. */
    fun MacApplicationMenu(content: MenuScope.() -> Unit) {
        if (hasApp) {
            println("[AdvancedMenuBar] MacApplicationMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Application(s.elements.toList())
        hasApp = true
    }

    /** Declares the single File menu. */
    fun FileMenu(
        title: String = getString(Res.string.file),
        content: MenuScope.() -> Unit,
    ) {
        if (hasFile) {
            println("[AdvancedMenuBar] FileMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.File(title, s.elements.toList())
        hasFile = true
    }

    /**
     * Declares the single Edit menu.
     *
     * Set [suppressAutomaticItems] to remove Writing Tools, AutoFill, Dictation, and Emoji &
     * Symbols entries that current macOS versions may append automatically.
     */
    fun EditMenu(
        title: String = getString(Res.string.edit),
        suppressAutomaticItems: Boolean = false,
        content: MenuScope.() -> Unit,
    ) {
        if (hasEdit) {
            println("[AdvancedMenuBar] EditMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Edit(title, s.elements.toList(), suppressAutomaticItems)
        hasEdit = true
    }

    /** Declares the single Format menu. */
    fun FormatMenu(
        title: String = getString(Res.string.format),
        content: MenuScope.() -> Unit,
    ) {
        if (hasFormat) {
            println("[AdvancedMenuBar] FormatMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Format(title, s.elements.toList())
        hasFormat = true
    }

    /** Declares the single View menu. */
    fun ViewMenu(
        title: String = getString(Res.string.view),
        content: MenuScope.() -> Unit,
    ) {
        if (hasView) {
            println("[AdvancedMenuBar] ViewMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.View(title, s.elements.toList())
        hasView = true
    }

    /**
     * Declares the single Window menu.
     *
     * Set [suppressAutoWindowList] when AppKit should not append its managed window list.
     */
    fun WindowMenu(
        title: String = getString(Res.string.window),
        suppressAutoWindowList: Boolean = false,
        content: MenuScope.() -> Unit,
    ) {
        if (hasWindow) {
            println("[AdvancedMenuBar] WindowMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Window(title, s.elements.toList(), suppressAutoWindowList)
        hasWindow = true
    }

    /** Declares the single Help menu. */
    fun HelpMenu(
        title: String = getString(Res.string.help),
        content: MenuScope.() -> Unit,
    ) {
        if (hasHelp) {
            println("[AdvancedMenuBar] HelpMenu already set – further call will be ignored.")
            return
        }
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Help(title, s.elements.toList())
        hasHelp = true
    }

    /** Declares an application-defined top-level menu. May be called more than once. */
    fun CustomMenu(
        title: String,
        content: MenuScope.() -> Unit,
    ) {
        val s = MenuScope(strings)
        s.content()
        menus += TopMenu.Custom(title, s.elements.toList())
    }
}
