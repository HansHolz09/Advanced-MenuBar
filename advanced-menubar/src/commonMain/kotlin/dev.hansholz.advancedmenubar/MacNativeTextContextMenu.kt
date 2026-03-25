@file:Suppress("SameParameterValue")

package dev.hansholz.advancedmenubar

import androidx.compose.ui.geometry.Rect
import com.sun.jna.Callback
import com.sun.jna.Library
import com.sun.jna.Memory
import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.Pointer
import com.sun.jna.Structure
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap
import javax.swing.SwingUtilities

internal object MacNativeTextContextMenu {

    private interface ObjC : Library {
        fun objc_getClass(name: String): Pointer?
        fun sel_registerName(name: String): Pointer?
        fun sel_getName(sel: Pointer): String?

        fun objc_msgSend(receiver: Pointer, selector: Pointer): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg: Pointer): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg: Long): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg: Double): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg1: Pointer, arg2: Pointer): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg1: Pointer, arg2: Pointer, arg3: Pointer): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, arg1: Pointer, arg2: Long): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, d1: Double, d2: Double): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, x: Double, y: Double, w: Double, h: Double): Pointer?
        fun objc_msgSend(receiver: Pointer, selector: Pointer, loc: Long, len: Long): Pointer?

        fun objc_allocateClassPair(supercls: Pointer, name: String, extraBytes: Long): Pointer?
        fun objc_registerClassPair(cls: Pointer)
        fun class_addMethod(cls: Pointer, name: Pointer, imp: Callback, types: String): Byte
    }

    private val objc: ObjC = Native.load("objc", ObjC::class.java)

    private val NIL = Pointer(0L)
    private fun sel(name: String): Pointer = objc.sel_registerName(name) ?: NIL


    private interface DispatchAsyncLib : Library { fun dispatch_async_f(q: Pointer?, ctx: Pointer?, fn: DispatchAsyncFn) }
    private interface DispatchAsyncFn  : Callback { fun invoke(ctx: Pointer?) }

    private val dispatchAsyncLib: DispatchAsyncLib? by lazy {
        listOf("dispatch", "System").firstNotNullOfOrNull { lib ->
            try { Native.load(lib, DispatchAsyncLib::class.java) } catch (_: Throwable) { null }
        }
    }
    private val dispatchNativeLib: NativeLibrary? by lazy {
        listOf("dispatch", "System").firstNotNullOfOrNull { lib ->
            try { NativeLibrary.getInstance(lib) } catch (_: Throwable) { null }
        }
    }


    private fun isNull(p: Pointer?): Boolean = p == null || Pointer.nativeValue(p) == 0L
    private fun nn(p: Pointer?): Pointer = if (p != null && Pointer.nativeValue(p) != 0L) p else NIL

    private fun msgSendP(recv: Pointer?, cmd: String): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s) } catch (_: Throwable) { null }
    }
    private fun msgSendPP(recv: Pointer?, cmd: String, a: Pointer?): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, nn(a)) } catch (_: Throwable) { null }
    }
    private fun msgSendPPP(recv: Pointer?, cmd: String, a: Pointer?, b: Pointer?): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, nn(a), nn(b)) } catch (_: Throwable) { null }
    }
    private fun msgSendPPPP(recv: Pointer?, cmd: String, a: Pointer?, b: Pointer?, c: Pointer?): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, nn(a), nn(b), nn(c)) } catch (_: Throwable) { null }
    }
    private fun msgSendPL(recv: Pointer?, cmd: String, l: Long): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, l) } catch (_: Throwable) { null }
    }
    private fun msgSendPPL(recv: Pointer?, cmd: String, a: Pointer?, l: Long): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, nn(a), l) } catch (_: Throwable) { null }
    }
    private fun msgSendPD(recv: Pointer?, cmd: String, d: Double): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, d) } catch (_: Throwable) { null }
    }
    private fun msgSendPDD(recv: Pointer?, cmd: String, d1: Double, d2: Double): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel(cmd);  if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, d1, d2) } catch (_: Throwable) { null }
    }
    private fun msgSendSetFrame(recv: Pointer?, x: Double, y: Double, w: Double, h: Double): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel("setFrame:"); if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, x, y, w, h) } catch (_: Throwable) { null }
    }
    private fun msgSendInitFrame(recv: Pointer?, x: Double, y: Double, w: Double, h: Double): Pointer? {
        val r = nn(recv); if (isNull(r)) return null
        val s = sel("initWithFrame:"); if (isNull(s)) return null
        return try { objc.objc_msgSend(r, s, x, y, w, h) } catch (_: Throwable) { null }
    }
    private fun msgSendSetRange(recv: Pointer?, loc: Long, len: Long) {
        val r = nn(recv); if (isNull(r)) return
        val s = sel("setSelectedRange:"); if (isNull(s)) return
        try { objc.objc_msgSend(r, s, loc, len) } catch (_: Throwable) {}
    }
    private fun msgSendContextMenu(menu: Pointer?, event: Pointer?, view: Pointer?): Boolean {
        val menuCls = objc.objc_getClass("NSMenu") ?: return false
        val m = nn(menu); if (isNull(m)) return false
        val e = nn(event); if (isNull(e)) return false
        val v = nn(view); if (isNull(v)) return false
        return try {
            msgSendPPPP(menuCls, "popUpContextMenu:withEvent:forView:", m, e, v)
            true
        } catch (t: Throwable) {
            System.err.println("[MacNativeTextContextMenu] popUpContextMenu: $t")
            false
        }
    }

    private fun intOf(recv: Pointer?, cmd: String): Int =
        Pointer.nativeValue(msgSendP(recv, cmd) ?: return 0).toInt()
    private fun boolOf(recv: Pointer?, cmd: String): Boolean =
        Pointer.nativeValue(msgSendP(recv, cmd) ?: return false) != 0L
    private fun nsStringToKotlin(nsStr: Pointer?): String {
        if (isNull(nsStr)) return ""
        val raw = msgSendP(nsStr, "UTF8String") ?: return ""
        return try { raw.getString(0, "UTF-8") } catch (_: Throwable) { "" }
    }


    private fun dispatchMainQueue(): Pointer {
        val lib = dispatchNativeLib ?: error("could not load libdispatch / libSystem")
        for (sym in listOf("_dispatch_main_q", "__dispatch_main_q")) {
            try { return lib.getGlobalVariableAddress(sym) } catch (_: UnsatisfiedLinkError) {}
        }
        error("main queue symbol not found")
    }

    internal fun dispatchAsyncOnMain(block: () -> Unit) {
        val q = dispatchMainQueue()
        dispatchAsyncLib!!.dispatch_async_f(q, null, object : DispatchAsyncFn { override fun invoke(ctx: Pointer?) = block() })
    }


    private fun nsString(str: String): Pointer? {
        val NSString = objc.objc_getClass("NSString") ?: return null
        val bytes = str.toByteArray(StandardCharsets.UTF_8)
        val mem = Memory((bytes.size + 1).toLong()).also {
            it.write(0, bytes, 0, bytes.size)
            it.setByte(bytes.size.toLong(), 0)
        }
        return msgSendPP(NSString, "stringWithUTF8String:", mem)
    }

    private fun nsApp(): Pointer? =
        msgSendP(objc.objc_getClass("NSApplication"), "sharedApplication")


    internal fun captureCurrentNsEvent(): Long {
        val app   = nsApp() ?: return 0L
        val event = msgSendP(app, "currentEvent") ?: return 0L
        return Pointer.nativeValue(event)
    }


    private const val ACTION_SEL = "invokeTextMenuItem:"
    private val menuActions = ConcurrentHashMap<Long, () -> Unit>()
    @Volatile private var actionTarget: Pointer? = null

    private interface MenuItemCallback : Callback {
        fun invoke(self: Pointer?, _cmd: Pointer?, sender: Pointer?)
    }
    private val menuItemCb = object : MenuItemCallback {
        override fun invoke(self: Pointer?, _cmd: Pointer?, sender: Pointer?) {
            menuActions[Pointer.nativeValue(nn(sender))]?.invoke()
        }
    }

    private fun ensureActionTarget(): Pointer? {
        actionTarget?.let { return it }
        synchronized(this) {
            actionTarget?.let { return it }
            val NSObject = objc.objc_getClass("NSObject") ?: return null
            val clsName  = "JNATextMenuTarget"
            var cls = objc.objc_allocateClassPair(NSObject, clsName, 0L)
            cls = if (isNull(cls)) objc.objc_getClass(clsName) else cls?.also {
                objc.class_addMethod(it, sel(ACTION_SEL), menuItemCb, "v@:@")
                objc.objc_registerClassPair(it)
            }
            return msgSendP(msgSendP(cls, "alloc"), "init").also { actionTarget = it }
        }
    }


    @Volatile private var textChangeCallback: ((String) -> Unit)? = null
    @Volatile private var delegateInstance:   Pointer? = null

    private interface TextDidChangeCallback : Callback {
        fun invoke(self: Pointer?, _cmd: Pointer?, notification: Pointer?)
    }
    private val textDidChangeCb = object : TextDidChangeCallback {
        override fun invoke(self: Pointer?, _cmd: Pointer?, notification: Pointer?) {
            val cb   = textChangeCallback ?: return
            val tv   = msgSendP(nn(notification), "object") ?: return
            val str  = msgSendP(tv, "string") ?: return
            val text = nsStringToKotlin(str)
            SwingUtilities.invokeLater { cb(text) }
        }
    }

    private fun ensureDelegate(): Pointer? {
        delegateInstance?.let { return it }
        synchronized(this) {
            delegateInstance?.let { return it }
            val NSObject = objc.objc_getClass("NSObject") ?: return null
            val clsName  = "JNATextViewDelegate"
            var cls = objc.objc_allocateClassPair(NSObject, clsName, 0L)
            cls = if (isNull(cls)) objc.objc_getClass(clsName) else cls?.also {
                objc.class_addMethod(it, sel("textDidChange:"), textDidChangeCb, "v@:@")
                objc.objc_registerClassPair(it)
            }
            return msgSendP(msgSendP(cls, "alloc"), "init").also { delegateInstance = it }
        }
    }


    class NSPoint : Structure() {
        override fun getFieldOrder(): List<String> = listOf("x", "y")
    }

    private interface HitTestCallback : Callback {
        fun invoke(self: Pointer?, _cmd: Pointer?, point: NSPoint?): Pointer?
    }
    private val hitTestCb = object : HitTestCallback {
        override fun invoke(self: Pointer?, _cmd: Pointer?, point: NSPoint?): Pointer? = null
    }

    @Volatile private var hiddenTextView: Pointer? = null
    @Volatile private var lastMenuDismissedAtMillis: Long = 0L

    private const val REOPEN_GUARD_MILLIS = 320L

    private val EXCLUDED_ACTION_SELECTORS = setOf(
        "pasteAsPlainText:",
        "orderFrontFontPanel:",
        "orderFrontColorPanel:",
        "showGuessPanel:",
        "checkSpelling:",
        "toggleContinuousSpellChecking:",
        "toggleGrammarChecking:",
        "toggleAutomaticSpellingCorrection:",
        "replaceQuotesInSelection:",
        "replaceDashesInSelection:",
        "replaceTextInSelection:",
        "orderFrontSubstitutionsPanel:",
        "toggleSmartInsertDelete:",
        "toggleAutomaticQuoteSubstitution:",
        "toggleAutomaticDashSubstitution:",
        "toggleAutomaticLinkDetection:",
        "toggleAutomaticDataDetection:",
        "toggleAutomaticTextReplacement:",
        "changeLayoutOrientation:"
    )

    private val NATIVE_EDIT_ACTIONS = setOf(
        "cut:",
        "copy:",
        "paste:",
        "selectAll:",
    )

    private val ROOT_STANDARD_ACTIONS = setOf(
        "cut:",
        "copy:",
        "paste:",
    )

    internal fun shouldSuppressSecondaryClickOpen(nowMillis: Long = System.currentTimeMillis()): Boolean {
        return nowMillis - lastMenuDismissedAtMillis < REOPEN_GUARD_MILLIS
    }

    private fun markMenuDismissedNow() {
        lastMenuDismissedAtMillis = System.currentTimeMillis()
    }

    private fun configureHiddenTextViewVisuals(tv: Pointer) {
        msgSendPL(tv, "setDrawsBackground:", 0L)
        try { msgSendPL(tv, "setFocusRingType:", 1L) } catch (_: Throwable) {}
        try { msgSendPD(tv, "setAlphaValue:", 0.0) } catch (_: Throwable) {}

        val clear = msgSendP(objc.objc_getClass("NSColor"), "clearColor")
        if (!isNull(clear)) {
            msgSendPP(tv, "setTextColor:", clear)
            try { msgSendPP(tv, "setInsertionPointColor:", clear) } catch (_: Throwable) {}
            try { msgSendPP(tv, "setBackgroundColor:", clear) } catch (_: Throwable) {}
        }

        try { msgSendPDD(tv, "setTextContainerInset:", 0.0, 0.0) } catch (_: Throwable) {}
        val textContainer = msgSendP(tv, "textContainer")
        if (!isNull(textContainer)) {
            try { msgSendPD(textContainer, "setLineFragmentPadding:", 0.0) } catch (_: Throwable) {}
        }
    }

    private fun ensureHiddenTextView(): Pointer? {
        hiddenTextView?.let { return it }
        synchronized(this) {
            hiddenTextView?.let { return it }

            val NSTextView = objc.objc_getClass("NSTextView") ?: run {
                System.err.println("[MacNativeTextContextMenu] NSTextView class not found"); return null
            }
            val subclsName = "JNANoHitTextView"
            var subcls = objc.objc_allocateClassPair(NSTextView, subclsName, 0L)
            subcls = if (isNull(subcls)) {
                objc.objc_getClass(subclsName)
            } else {
                subcls?.also {
                    objc.class_addMethod(it, sel("hitTest:"), hitTestCb, "@@:{NSPoint=dd}")
                    objc.objc_registerClassPair(it)
                }
            }
            val tv = msgSendInitFrame(msgSendP(subcls, "alloc"), 0.0, 0.0, 1.0, 1.0) ?: run {
                System.err.println("[MacNativeTextContextMenu] initWithFrame: returned nil"); return null
            }
            msgSendPL(tv, "setEditable:", 1L)
            msgSendPL(tv, "setSelectable:", 1L)
            msgSendPL(tv, "setRichText:", 0L)
            configureHiddenTextViewVisuals(tv)
            hiddenTextView = tv
            return tv
        }
    }

    private fun detachHiddenTextView() {
        val tv = hiddenTextView ?: return
        textChangeCallback = null
        msgSendPP(tv, "setDelegate:", NIL)
        msgSendP(tv, "removeFromSuperview")
    }

    private fun retainHiddenTextViewAsAnchor(
        tv: Pointer,
        fieldX: Double,
        fieldY: Double,
        fieldW: Double,
        fieldH: Double,
    ) {
        val centerY = fieldY + fieldH / 2.0
        msgSendSetFrame(tv, fieldX, centerY, fieldW, 1.0)
        configureHiddenTextViewVisuals(tv)
    }

    private fun makeAppearance(isDark: Boolean): Pointer? {
        val NSAppearance = objc.objc_getClass("NSAppearance") ?: return null
        val name = nsString(if (isDark) "NSAppearanceNameDarkAqua" else "NSAppearanceNameAqua") ?: return null
        return msgSendPP(NSAppearance, "appearanceNamed:", name)
    }

    private fun setAppearance(target: Pointer?, appearance: Pointer?) {
        if (isNull(target) || isNull(appearance)) return
        try { msgSendPP(target, "setAppearance:", appearance) } catch (_: Throwable) {}
    }

    private fun applyAppearanceRecursive(menu: Pointer?, appearance: Pointer?) {
        if (isNull(menu) || isNull(appearance)) return
        setAppearance(menu, appearance)
        val count = intOf(menu, "numberOfItems")
        for (i in 0 until count) {
            val item = msgSendPL(menu, "itemAtIndex:", i.toLong()) ?: continue
            val sub = msgSendP(item, "submenu") ?: continue
            if (!isNull(sub)) applyAppearanceRecursive(sub, appearance)
        }
    }

    internal fun applyAppearance(isDark: Boolean) {
        val appearance = makeAppearance(isDark) ?: return
        val app = nsApp()
        setAppearance(app, appearance)
    }

    private fun itemActionName(item: Pointer?): String {
        val actionSel = msgSendP(item, "action") ?: return ""
        return objc.sel_getName(actionSel).orEmpty()
    }

    private fun removeItemsMatching(menu: Pointer, shouldRemove: (Pointer, String) -> Boolean) {
        val count = intOf(menu, "numberOfItems")
        val toRemove = mutableListOf<Pointer>()
        for (i in 0 until count) {
            val item = msgSendPL(menu, "itemAtIndex:", i.toLong()) ?: continue
            if (boolOf(item, "isSeparatorItem")) continue
            val actionName = itemActionName(item)
            val submenu = msgSendP(item, "submenu")
            if (submenu != null && !isNull(submenu)) {
                removeItemsMatching(submenu, shouldRemove)
                cleanupSeparators(submenu)
                if (intOf(submenu, "numberOfItems") == 0) {
                    toRemove.add(item)
                    continue
                }
            }
            if (shouldRemove(item, actionName)) toRemove.add(item)
        }
        for (item in toRemove) msgSendPP(menu, "removeItem:", item)
        cleanupSeparators(menu)
    }

    private fun removeExcludedItems(menu: Pointer) {
        if (EXCLUDED_ACTION_SELECTORS.isEmpty()) return
        removeItemsMatching(menu) { _, actionName -> actionName in EXCLUDED_ACTION_SELECTORS }
    }

    private fun keepOnlyStandardRootItems(menu: Pointer) {
        val count = intOf(menu, "numberOfItems")
        val toRemove = mutableListOf<Pointer>()
        for (i in 0 until count) {
            val item = msgSendPL(menu, "itemAtIndex:", i.toLong()) ?: continue
            if (boolOf(item, "isSeparatorItem")) {
                toRemove.add(item)
                continue
            }
            val actionName = itemActionName(item)
            if (actionName !in NATIVE_EDIT_ACTIONS) toRemove.add(item)
        }
        for (item in toRemove) msgSendPP(menu, "removeItem:", item)
        cleanupSeparators(menu)
    }

    private fun findInsertIndexAfterPaste(menu: Pointer): Int {
        val count = intOf(menu, "numberOfItems")
        var lastStandardIndex = -1
        for (i in 0 until count) {
            val item = msgSendPL(menu, "itemAtIndex:", i.toLong()) ?: continue
            if (boolOf(item, "isSeparatorItem")) {
                if (lastStandardIndex >= 0) return lastStandardIndex + 1
                continue
            }
            val actionName = itemActionName(item)
            if (actionName in ROOT_STANDARD_ACTIONS || actionName == "selectAll:") {
                lastStandardIndex = i
            } else if (lastStandardIndex >= 0) {
                return lastStandardIndex + 1
            }
        }
        return if (lastStandardIndex >= 0) lastStandardIndex + 1 else 0
    }

    private fun cleanupSeparators(menu: Pointer) {
        var dirty = true
        while (dirty) {
            dirty = false
            val count = intOf(menu, "numberOfItems")
            for (i in 0 until count) {
                val item = msgSendPL(menu, "itemAtIndex:", i.toLong()) ?: continue
                if (!boolOf(item, "isSeparatorItem")) continue
                val remove = i == 0 || i == count - 1 ||
                        boolOf(msgSendPL(menu, "itemAtIndex:", (i - 1).toLong()), "isSeparatorItem")
                if (remove) { msgSendPP(menu, "removeItem:", item); dirty = true; break }
            }
        }
    }


    private fun newCallbackItem(title: String, systemImageName: String?, target: Pointer, enabled: Boolean, action: () -> Unit): Pointer? {
        val NSMenuItem = objc.objc_getClass("NSMenuItem") ?: return null
        val item = msgSendP(msgSendP(NSMenuItem, "alloc"), "init") ?: return null
        msgSendPP(item, "setTitle:",         nsString(title) ?: return null)
        msgSendPP(item, "setKeyEquivalent:", nsString("") ?: NIL)
        msgSendPP(item, "setTarget:",        target)
        msgSendPL(item, "setEnabled:",       if (enabled) 1 else 0)
        if (enabled) msgSendPP(item, "setAction:", sel(ACTION_SEL))
        setMenuItemSystemImage(item, systemImageName)
        menuActions[Pointer.nativeValue(item)] = action
        return item
    }

    private fun setMenuItemSystemImage(item: Pointer, systemImageName: String?) {
        if (systemImageName.isNullOrBlank()) return
        val NSImage = objc.objc_getClass("NSImage") ?: return
        val symbolName = nsString(systemImageName) ?: return
        val accessibility = nsString("") ?: NIL
        val image = msgSendPPP(NSImage, "imageWithSystemSymbolName:accessibilityDescription:", symbolName, accessibility)
            ?: return
        if (!isNull(image)) {
            try { msgSendPL(image, "setTemplate:", 1L) } catch (_: Throwable) {}
            msgSendPP(item, "setImage:", image)
        }
    }

    private fun separatorItem(): Pointer? =
        msgSendP(objc.objc_getClass("NSMenuItem"), "separatorItem")

    private fun insertItem(menu: Pointer?, item: Pointer?, atIndex: Int) {
        if (!isNull(menu) && !isNull(item))
            msgSendPPL(menu, "insertItem:atIndex:", item, atIndex.toLong())
    }

    private fun insertSeparator(menu: Pointer?, atIndex: Int) =
        insertItem(menu, separatorItem(), atIndex)

    internal fun showForTextField(
        selectedText: String,
        nsEventAddress: Long,
        contentHeightPts: Double,
        contentWidthPts: Double,
        fieldRect: Rect,
        selectAllAction: ContextMenuAction,
        customActions: List<ContextMenuAction>,
        showExtraOptions: Boolean,
        onTextChange: ((String) -> Unit)?,
    ) {
        val rawX = fieldRect.left.toDouble()
        val rawY = contentHeightPts - fieldRect.bottom.toDouble()
        val rawW = fieldRect.width.toDouble().coerceAtLeast(1.0)
        val rawH = fieldRect.height.toDouble().coerceAtLeast(1.0)

        val fieldX = rawX.coerceIn(0.0, (contentWidthPts  - 1.0).coerceAtLeast(0.0))
        val fieldY = rawY.coerceIn(0.0, (contentHeightPts - 1.0).coerceAtLeast(0.0))
        val fieldW = (rawW - (fieldX - rawX)).coerceAtMost(contentWidthPts  - fieldX).coerceAtLeast(1.0)
        val fieldH = (rawH - (fieldY - rawY)).coerceAtMost(contentHeightPts - fieldY).coerceAtLeast(1.0)

        run {
            val tv = ensureHiddenTextView() ?: return@run
            detachHiddenTextView()

            textChangeCallback = null
            msgSendPP(tv, "setDelegate:", NIL)
            configureHiddenTextViewVisuals(tv)

            val hostText = if (selectedText.isNotEmpty()) selectedText else " "
            nsString(hostText)?.let { msgSendPP(tv, "setString:", it) }
            msgSendSetRange(tv, 0L, selectedText.length.toLong().coerceAtLeast(0L))

            val app         = nsApp()
            val nsWindow    = msgSendP(app, "keyWindow")
            val contentView = msgSendP(nsWindow, "contentView") ?: run {
                System.err.println("[MacNativeTextContextMenu] keyWindow has no contentView"); return
            }

            msgSendSetFrame(tv, fieldX, fieldY, fieldW, fieldH)
            msgSendPP(contentView, "addSubview:", tv)
            msgSendP(tv, "layoutSubtreeIfNeeded")
            msgSendP(tv, "displayIfNeeded")

            val prevFirstResponder = msgSendP(nsWindow, "firstResponder")
            msgSendPP(nsWindow, "makeFirstResponder:", tv)

            val nsEvent = if (nsEventAddress != 0L) Pointer(nsEventAddress) else NIL
            val menu    = msgSendPP(tv, "menuForEvent:", nsEvent) ?: run {
                System.err.println("[MacNativeTextContextMenu] menuForEvent: returned nil")
                finalizeMenuPresentation(
                    tv, nsWindow, prevFirstResponder,
                    fieldX, fieldY, fieldW, fieldH,
                    keepAnchor = false
                )
                return
            }

            if (showExtraOptions) {
                removeExcludedItems(menu)
            } else {
                keepOnlyStandardRootItems(menu)
            }

            menuActions.clear()
            val target = ensureActionTarget() ?: run {
                finalizeMenuPresentation(
                    tv, nsWindow, prevFirstResponder,
                    fieldX, fieldY, fieldW, fieldH,
                    keepAnchor = false
                )
                return
            }

            val insertIndex = findInsertIndexAfterPaste(menu)
            insertItem(
                menu,
                newCallbackItem(
                    selectAllAction.label,
                    selectAllAction.systemImageName,
                    target,
                    selectAllAction.enabled,
                    selectAllAction.action
                ),
                insertIndex
            )

            insertSeparator(menu, insertIndex + 1)
            customActions.forEachIndexed { index, action ->
                insertItem(
                    menu,
                    newCallbackItem(
                        action.label,
                        action.systemImageName,
                        target,
                        action.enabled,
                        action.action
                    ),
                    insertIndex + index + 2
                )
            }

            if (showExtraOptions) {
                val separatorIndex = insertIndex + 1
                if (intOf(menu, "numberOfItems") > separatorIndex) {
                    val next = msgSendPL(menu, "itemAtIndex:", separatorIndex.toLong())
                    if (next != null && !boolOf(next, "isSeparatorItem")) insertSeparator(menu, separatorIndex)
                }
            }

            cleanupSeparators(menu)

            if (intOf(menu, "numberOfItems") == 0) {
                finalizeMenuPresentation(
                    tv, nsWindow, prevFirstResponder,
                    fieldX, fieldY, fieldW, fieldH,
                    keepAnchor = false
                )
                return@run
            }

            if (onTextChange != null) {
                textChangeCallback = onTextChange
                ensureDelegate()?.let { msgSendPP(tv, "setDelegate:", it) }
            }

            msgSendContextMenu(menu, nsEvent, tv)

            markMenuDismissedNow()
            finalizeMenuPresentation(
                tv, nsWindow, prevFirstResponder,
                fieldX, fieldY, fieldW, fieldH,
                keepAnchor = true
            )
        }
    }

    private fun finalizeMenuPresentation(
        tv: Pointer,
        nsWindow: Pointer?,
        prevFirstResponder: Pointer?,
        fieldX: Double,
        fieldY: Double,
        fieldW: Double,
        fieldH: Double,
        keepAnchor: Boolean,
    ) {
        if (!isNull(prevFirstResponder)) {
            msgSendPP(nsWindow, "makeFirstResponder:", prevFirstResponder)
        }
        if (keepAnchor) {
            retainHiddenTextViewAsAnchor(tv, fieldX, fieldY, fieldW, fieldH)
        } else {
            detachHiddenTextView()
        }
    }
}