#import <Cocoa/Cocoa.h>
#import <jni.h>
#import <stdio.h>
#import <stdlib.h>
#import <string.h>

static JavaVM *gJavaVm = NULL;
static jclass gBridgeClass = NULL;
static jmethodID gOnAction = NULL;
static jclass gTextBridgeClass = NULL;
static jmethodID gTextOnAction = NULL;
static jmethodID gTextOnChanged = NULL;
static jmethodID gTextOnDismissed = NULL;
static jclass gTaoBridgeClass = NULL;
static jmethodID gTaoWake = NULL;
static NSEvent *gCapturedContextEvent = nil;
static BOOL gTextContextMenuScheduledOrTracking = NO;
static id gTextContextMouseUpMonitor = nil;
static NSTextView *gRetainedTextContextView = nil;
static NSMenu *gRetainedTextContextMenu = nil;
static id gRetainedTextContextDelegate = nil;
static id gAutomaticEditMenuDelegate = nil;

static BOOL ambDebugEnabled(void) {
    static BOOL enabled;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        const char *value = getenv("ADVANCED_MENUBAR_DEBUG");
        enabled = value != NULL && value[0] != '\0' && strcmp(value, "0") != 0;
    });
    return enabled;
}

static void ambDebugMenu(NSMenu *menu, NSString *prefix) {
    if (!ambDebugEnabled() || menu == nil) return;
    for (NSMenuItem *item in menu.itemArray) {
        fprintf(stderr, "[AdvancedMenubar] %s title=%s action=%s enabled=%d target=%s\n",
                prefix.UTF8String ?: "menu",
                item.title.UTF8String ?: "",
                item.action == NULL ? "" : NSStringFromSelector(item.action).UTF8String,
                item.enabled,
                item.target == nil ? "(responder chain)" : NSStringFromClass([item.target class]).UTF8String);
        if (item.submenu != nil) ambDebugMenu(item.submenu, @"submenu");
    }
}

static JNIEnv *ambEnv(void) {
    if (gJavaVm == NULL) return NULL;
    JNIEnv *env = NULL;
    jint result = (*gJavaVm)->GetEnv(gJavaVm, (void **)&env, JNI_VERSION_1_6);
    if (result == JNI_EDETACHED) {
        if ((*gJavaVm)->AttachCurrentThread(gJavaVm, (void **)&env, NULL) != JNI_OK) return NULL;
    }
    return env;
}

static void ambWakeTaoEventLoop(void) {
    if (gTaoBridgeClass == NULL || gTaoWake == NULL) return;
    JNIEnv *env = ambEnv();
    if (env == NULL) return;
    (*env)->CallStaticVoidMethod(env, gTaoBridgeClass, gTaoWake);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionDescribe(env);
        (*env)->ExceptionClear(env);
    }
}

@interface AdvancedMenubarActionTarget : NSObject
+ (instancetype)shared;
- (void)invokeMenuItem:(id)sender;
@end

@interface AdvancedMenubarNoHitTextView : NSTextView
@end

@implementation AdvancedMenubarNoHitTextView
- (NSView *)hitTest:(NSPoint)point { (void)point; return nil; }
@end

@interface AdvancedMenubarTextDelegate : NSObject <NSTextViewDelegate>
@property(nonatomic) int64_t callbackId;
@property(nonatomic, copy) NSString *originalText;
@property(nonatomic, copy) NSString *changedText;
@end

@implementation AdvancedMenubarTextDelegate
- (void)textDidChange:(NSNotification *)notification {
    NSString *text = [(NSTextView *)notification.object string] ?: @"";
    self.changedText = [text isEqualToString:self.originalText ?: @""] ? nil : text;
}
@end

@interface AdvancedMenubarTextActionTarget : NSObject
+ (instancetype)shared;
- (void)invokeTextMenuItem:(id)sender;
@end


@implementation AdvancedMenubarTextActionTarget
+ (instancetype)shared {
    static AdvancedMenubarTextActionTarget *shared;
    static dispatch_once_t once;
    dispatch_once(&once, ^{ shared = [[AdvancedMenubarTextActionTarget alloc] init]; });
    return shared;
}
- (void)invokeTextMenuItem:(NSMenuItem *)sender {
    int64_t callbackId = [sender.representedObject longLongValue];
    dispatch_async(dispatch_get_main_queue(), ^{
        JNIEnv *env = ambEnv();
        if (env == NULL || gTextBridgeClass == NULL || gTextOnAction == NULL) return;
        (*env)->CallStaticVoidMethod(env, gTextBridgeClass, gTextOnAction, (jlong)callbackId);
        if ((*env)->ExceptionCheck(env)) {
            (*env)->ExceptionDescribe(env);
            (*env)->ExceptionClear(env);
        }
    });
}
@end

static void ambConfigureHiddenTextView(NSTextView *view) {
    view.drawsBackground = NO;
    view.focusRingType = NSFocusRingTypeNone;
    view.alphaValue = 0.0;
    view.textColor = NSColor.clearColor;
    view.insertionPointColor = NSColor.clearColor;
    view.backgroundColor = NSColor.clearColor;
    view.textContainerInset = NSZeroSize;
    view.textContainer.lineFragmentPadding = 0.0;
}

static AdvancedMenubarNoHitTextView *ambCreateHiddenTextView(BOOL editable) {
    AdvancedMenubarNoHitTextView *view =
        [[AdvancedMenubarNoHitTextView alloc] initWithFrame:NSMakeRect(0, 0, 1, 1)];
    view.editable = editable;
    view.selectable = YES;
    view.richText = NO;
    ambConfigureHiddenTextView(view);
    return view;
}

static NSString *ambActionName(NSMenuItem *item) {
    return item.action == NULL ? @"" : NSStringFromSelector(item.action);
}

static void ambCleanupSeparators(NSMenu *menu) {
    BOOL changed = YES;
    while (changed) {
        changed = NO;
        for (NSInteger i = 0; i < menu.numberOfItems; i++) {
            NSMenuItem *item = [menu itemAtIndex:i];
            if (!item.separatorItem) continue;
            BOOL remove = i == 0 || i == menu.numberOfItems - 1 || [menu itemAtIndex:i - 1].separatorItem;
            if (remove) {
                [menu removeItem:item];
                changed = YES;
                break;
            }
        }
    }
}

static NSSet<NSString *> *ambExcludedTextActions(void) {
    static NSSet *actions;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        actions = [NSSet setWithArray:@[
            @"pasteAsPlainText:", @"orderFrontFontPanel:", @"orderFrontColorPanel:",
            @"showGuessPanel:", @"checkSpelling:", @"toggleContinuousSpellChecking:",
            @"toggleGrammarChecking:", @"toggleAutomaticSpellingCorrection:",
            @"replaceQuotesInSelection:", @"replaceDashesInSelection:", @"replaceTextInSelection:",
            @"orderFrontSubstitutionsPanel:", @"toggleSmartInsertDelete:",
            @"toggleAutomaticQuoteSubstitution:", @"toggleAutomaticDashSubstitution:",
            @"toggleAutomaticLinkDetection:", @"toggleAutomaticDataDetection:",
            @"toggleAutomaticTextReplacement:", @"changeLayoutOrientation:"
        ]];
    });
    return actions;
}

static void ambRemoveExcludedTextItems(NSMenu *menu) {
    NSArray *copy = menu.itemArray.copy;
    for (NSMenuItem *item in copy) {
        if (item.submenu != nil) {
            ambRemoveExcludedTextItems(item.submenu);
            ambCleanupSeparators(item.submenu);
            if (item.submenu.numberOfItems == 0) [menu removeItem:item];
        } else if (!item.separatorItem && [ambExcludedTextActions() containsObject:ambActionName(item)]) {
            [menu removeItem:item];
        }
    }
    ambCleanupSeparators(menu);
}

static void ambKeepBasicTextItems(NSMenu *menu) {
    NSSet *allowed = [NSSet setWithArray:@[@"cut:", @"copy:", @"paste:"]];
    for (NSMenuItem *item in menu.itemArray.copy) {
        if (item.separatorItem || ![allowed containsObject:ambActionName(item)]) [menu removeItem:item];
    }
    ambCleanupSeparators(menu);
}

static BOOL ambMenuContainsAnyAction(NSMenu *menu, NSSet<NSString *> *actions) {
    for (NSMenuItem *item in menu.itemArray) {
        if ([actions containsObject:ambActionName(item)]) return YES;
        if (item.submenu != nil && ambMenuContainsAnyAction(item.submenu, actions)) return YES;
    }
    return NO;
}

static void ambRemoveSelectionOnlyTextItems(NSMenu *menu) {
    static NSSet<NSString *> *actions;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        actions = [NSSet setWithArray:@[
            @"uppercaseWord:", @"lowercaseWord:", @"capitalizeWord:",
            @"startSpeaking:", @"stopSpeaking:"
        ]];
    });
    for (NSMenuItem *item in menu.itemArray.copy) {
        if (item.submenu != nil && ambMenuContainsAnyAction(item.submenu, actions)) {
            [menu removeItem:item];
        }
    }
    ambCleanupSeparators(menu);
}

static void ambRemoveReadOnlyEditingItems(NSMenu *menu) {
    static NSSet<NSString *> *actions;
    static dispatch_once_t once;
    dispatch_once(&once, ^{
        actions = [NSSet setWithArray:@[@"cut:", @"paste:", @"selectAll:"]];
    });
    for (NSMenuItem *item in menu.itemArray.copy) {
        if (item.submenu != nil) {
            ambRemoveReadOnlyEditingItems(item.submenu);
            if (item.submenu.numberOfItems == 0) [menu removeItem:item];
        } else if (!item.separatorItem && [actions containsObject:ambActionName(item)]) {
            [menu removeItem:item];
        }
    }
    ambCleanupSeparators(menu);
}

static void ambReportTextChange(int64_t callbackId, NSString *text) {
    if (callbackId == 0 || text == nil) return;
    JNIEnv *env = ambEnv();
    if (env == NULL || gTextBridgeClass == NULL || gTextOnChanged == NULL) return;
    jstring value = (*env)->NewStringUTF(env, text.UTF8String ?: "");
    (*env)->CallStaticVoidMethod(env, gTextBridgeClass, gTextOnChanged,
                                (jlong)callbackId, value);
    (*env)->DeleteLocalRef(env, value);
    if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionDescribe(env);
        (*env)->ExceptionClear(env);
    }
}

static NSInteger ambTextInsertIndex(NSMenu *menu) {
    NSSet *standard = [NSSet setWithArray:@[@"cut:", @"copy:", @"paste:", @"selectAll:"]];
    NSInteger last = -1;
    for (NSInteger i = 0; i < menu.numberOfItems; i++) {
        NSMenuItem *item = [menu itemAtIndex:i];
        if ([standard containsObject:ambActionName(item)]) last = i;
        else if (last >= 0) return last + 1;
    }
    return last >= 0 ? last + 1 : 0;
}

static NSMenuItem *ambTextCallbackItem(NSString *title,
                                       NSString *symbolName,
                                       BOOL enabled,
                                       int64_t callbackId) {
    NSMenuItem *item = [[NSMenuItem alloc] initWithTitle:title ?: @"" action:nil keyEquivalent:@""];
    item.enabled = enabled;
    if (enabled) {
        item.target = [AdvancedMenubarTextActionTarget shared];
        item.action = @selector(invokeTextMenuItem:);
    }
    item.representedObject = @(callbackId);
    if (symbolName.length > 0 && [NSImage respondsToSelector:@selector(imageWithSystemSymbolName:accessibilityDescription:)]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability-new"
        item.image = [NSImage imageWithSystemSymbolName:symbolName accessibilityDescription:nil];
#pragma clang diagnostic pop
        item.image.template = YES;
    }
    return item;
}

@implementation AdvancedMenubarActionTarget
+ (instancetype)shared {
    static AdvancedMenubarActionTarget *shared;
    static dispatch_once_t once;
    dispatch_once(&once, ^{ shared = [[AdvancedMenubarActionTarget alloc] init]; });
    return shared;
}
- (void)invokeMenuItem:(NSMenuItem *)sender {
    NSNumber *value = sender.representedObject;
    if (![value isKindOfClass:[NSNumber class]]) return;
    int64_t actionId = value.longLongValue;
    dispatch_async(dispatch_get_main_queue(), ^{
        JNIEnv *env = ambEnv();
        if (env == NULL || gBridgeClass == NULL || gOnAction == NULL) return;
        (*env)->CallStaticVoidMethod(env, gBridgeClass, gOnAction, (jlong)actionId);
        if ((*env)->ExceptionCheck(env)) {
            (*env)->ExceptionDescribe(env);
            (*env)->ExceptionClear(env);
        }
    });
}
@end

@interface AdvancedMenubarMenuState : NSObject
@property(nonatomic, strong) NSMenu *mainMenu;
@property(nonatomic, strong) NSMenu *servicesMenu;
@property(nonatomic, strong) NSMenu *windowsMenu;
@property(nonatomic, strong) NSMenu *helpMenu;
@end

@implementation AdvancedMenubarMenuState
@end

@interface AdvancedMenubarEditMenuBridge : NSObject
+ (instancetype)shared;
- (void)invokeEditMenuItem:(id)sender;
@end

@implementation AdvancedMenubarEditMenuBridge
+ (instancetype)shared {
    static AdvancedMenubarEditMenuBridge *shared;
    static dispatch_once_t once;
    dispatch_once(&once, ^{ shared = [[AdvancedMenubarEditMenuBridge alloc] init]; });
    return shared;
}
- (void)invokeEditMenuItem:(NSMenuItem *)sender {
    NSInteger command = [sender.representedObject integerValue];
    static const unsigned short keyCodes[] = {6, 6, 7, 8, 9, 9, 51, 0, 3, 3, 5, 5, 14, 38};
    static NSString *const characters[] = {@"z", @"z", @"x", @"c", @"v", @"v", @"\x7f", @"a", @"f", @"f", @"g", @"g", @"e", @"j"};
    if (command < 0 || command >= (NSInteger)(sizeof(keyCodes) / sizeof(keyCodes[0]))) return;

    NSEventModifierFlags modifiers = 0;
    if (command != 6) modifiers |= NSEventModifierFlagCommand;
    if (command == 1 || command == 11) modifiers |= NSEventModifierFlagShift;
    if (command == 5) modifiers |= NSEventModifierFlagShift | NSEventModifierFlagOption;
    if (command == 9) modifiers |= NSEventModifierFlagOption;

    unsigned short keyCode = keyCodes[command];
    NSString *chars = characters[command];
    dispatch_async(dispatch_get_main_queue(), ^{
        NSWindow *window = NSApp.keyWindow;
        if (window == nil) return;
        NSTimeInterval timestamp = NSProcessInfo.processInfo.systemUptime;
        NSEvent *down = [NSEvent keyEventWithType:NSEventTypeKeyDown
                                         location:NSZeroPoint
                                    modifierFlags:modifiers
                                        timestamp:timestamp
                                     windowNumber:window.windowNumber
                                          context:nil
                                       characters:chars
                      charactersIgnoringModifiers:chars
                                        isARepeat:NO
                                          keyCode:keyCode];
        NSEvent *up = [NSEvent keyEventWithType:NSEventTypeKeyUp
                                       location:NSZeroPoint
                                  modifierFlags:modifiers
                                      timestamp:timestamp
                                   windowNumber:window.windowNumber
                                        context:nil
                                     characters:chars
                    charactersIgnoringModifiers:chars
                                      isARepeat:NO
                                        keyCode:keyCode];
        if (down != nil) [window sendEvent:down];
        if (up != nil) [window sendEvent:up];
    });
}
@end

static NSView *ambFindTextInputView(NSView *root) {
    if (root == nil) return nil;
    if ([root conformsToProtocol:@protocol(NSTextInputClient)] && root.inputContext != nil) {
        return root;
    }
    for (NSView *subview in root.subviews) {
        NSView *candidate = ambFindTextInputView(subview);
        if (candidate != nil) return candidate;
    }
    return nil;
}

@interface AdvancedMenubarSystemEditTarget : NSObject
+ (instancetype)shared;
- (void)startDictationFromMenu:(NSMenuItem *)sender;
@end

@implementation AdvancedMenubarSystemEditTarget
+ (instancetype)shared {
    static AdvancedMenubarSystemEditTarget *shared;
    static dispatch_once_t once;
    dispatch_once(&once, ^{ shared = [[AdvancedMenubarSystemEditTarget alloc] init]; });
    return shared;
}
- (void)startDictationFromMenu:(NSMenuItem *)sender {
    NSWindow *window = NSApp.keyWindow;
    NSView *inputView = [window.firstResponder isKindOfClass:NSView.class]
        ? (NSView *)window.firstResponder
        : nil;
    if (![inputView conformsToProtocol:@protocol(NSTextInputClient)]) {
        inputView = ambFindTextInputView(window.contentView);
    }
    if (inputView != nil) {
        [window makeFirstResponder:inputView];
        [inputView.inputContext activate];
    }
    if (ambDebugEnabled() && inputView != nil) {
        id<NSTextInputClient> client = (id<NSTextInputClient>)inputView;
        NSRange actual = NSMakeRange(NSNotFound, 0);
        NSRange selection = [client selectedRange];
        NSRect rect = [client firstRectForCharacterRange:selection actualRange:&actual];
        fprintf(stderr,
                "[AdvancedMenubar] dictation input=%s selection={%lu,%lu} actual={%lu,%lu} rect={{%.1f,%.1f},{%.1f,%.1f}} context=%s\n",
                NSStringFromClass(inputView.class).UTF8String,
                (unsigned long)selection.location, (unsigned long)selection.length,
                (unsigned long)actual.location, (unsigned long)actual.length,
                rect.origin.x, rect.origin.y, rect.size.width, rect.size.height,
                inputView.inputContext.description.UTF8String ?: "(nil)");
    }

    sender.action = @selector(startDictation:);
    sender.target = NSApp;
    dispatch_async(dispatch_get_main_queue(), ^{
        [NSApp sendAction:@selector(startDictation:) to:NSApp from:sender];
    });
}
@end

typedef struct {
    const uint8_t *bytes;
    size_t length;
    size_t offset;
    BOOL valid;
} AmbReader;

static uint32_t ambReadU32(AmbReader *reader) {
    if (!reader->valid || reader->offset + 4 > reader->length) {
        reader->valid = NO;
        return 0;
    }
    const uint8_t *p = reader->bytes + reader->offset;
    reader->offset += 4;
    return ((uint32_t)p[0] << 24) | ((uint32_t)p[1] << 16) | ((uint32_t)p[2] << 8) | p[3];
}

static int32_t ambReadI32(AmbReader *reader) { return (int32_t)ambReadU32(reader); }

static int64_t ambReadI64(AmbReader *reader) {
    uint64_t high = ambReadU32(reader);
    uint64_t low = ambReadU32(reader);
    return (int64_t)((high << 32) | low);
}

static BOOL ambReadBool(AmbReader *reader) {
    if (!reader->valid || reader->offset + 1 > reader->length) {
        reader->valid = NO;
        return NO;
    }
    return reader->bytes[reader->offset++] != 0;
}

static NSString *ambReadString(AmbReader *reader) {
    int32_t length = ambReadI32(reader);
    if (length < 0) return nil;
    if (!reader->valid || reader->offset + (size_t)length > reader->length) {
        reader->valid = NO;
        return nil;
    }
    NSString *value = [[NSString alloc] initWithBytes:reader->bytes + reader->offset
                                              length:(NSUInteger)length
                                            encoding:NSUTF8StringEncoding];
    reader->offset += (size_t)length;
    return value ?: @"";
}

static NSImage *ambImage(int32_t kind, NSString *value, BOOL isTemplate) {
    if (kind == 0 || value.length == 0) return nil;
    NSImage *image = nil;
    if (kind == 1) {
        if ([NSImage respondsToSelector:@selector(imageWithSystemSymbolName:accessibilityDescription:)]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability-new"
            image = [NSImage imageWithSystemSymbolName:value accessibilityDescription:nil];
#pragma clang diagnostic pop
        }
    } else if (kind == 2) {
        image = [[NSImage alloc] initWithContentsOfFile:value];
    } else if (kind == 3) {
        NSData *data = [[NSData alloc] initWithBase64EncodedString:value options:0];
        if (data != nil) image = [[NSImage alloc] initWithData:data];
    }
    if (image != nil && (kind == 2 || kind == 3) &&
        [NSImage respondsToSelector:@selector(imageWithSystemSymbolName:accessibilityDescription:)]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability-new"
        NSImage *reference = [NSImage imageWithSystemSymbolName:@"circle"
                                       accessibilityDescription:nil];
#pragma clang diagnostic pop
        if (reference.size.width > 0.0 && reference.size.height > 0.0) {
            NSSize sourceSize = image.size;
            if (sourceSize.width > 0.0 && sourceSize.height > 0.0) {
                CGFloat scale = MIN(reference.size.width / sourceSize.width,
                                    reference.size.height / sourceSize.height);
                image.size = NSMakeSize(sourceSize.width * scale, sourceSize.height * scale);
            }
        }
    }
    image.template = isTemplate;
    return image;
}

static void ambConfigureItem(NSMenuItem *item,
                             NSString *selectorName,
                             NSString *keyEquivalent,
                             int32_t modifiers,
                             BOOL enabled,
                             int32_t state,
                             int32_t targetKind,
                             int64_t actionValue,
                             int32_t iconKind,
                             NSString *iconValue,
                             BOOL iconTemplate,
                             NSString *subtitle,
                             NSString *tooltip,
                             NSString *badge) {
    item.enabled = enabled;
    item.keyEquivalent = keyEquivalent ?: @"";
    if (keyEquivalent.length > 0) item.keyEquivalentModifierMask = (NSEventModifierFlags)modifiers;
    if (state >= 0) item.state = state == 0 ? NSControlStateValueOff : NSControlStateValueOn;
    item.image = ambImage(iconKind, iconValue, iconTemplate);
    item.toolTip = tooltip;
    if (subtitle != nil && [item respondsToSelector:@selector(setSubtitle:)]) {
        [item setValue:subtitle forKey:@"subtitle"];
    }
    if (badge != nil) {
        Class badgeClass = NSClassFromString(@"NSMenuItemBadge");
        SEL initializer = NSSelectorFromString(@"initWithString:");
        if (badgeClass != Nil && [badgeClass instancesRespondToSelector:initializer]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
            id value = [[badgeClass alloc] performSelector:initializer withObject:badge];
#pragma clang diagnostic pop
            [item setValue:value forKey:@"badge"];
        }
    }

    if (targetKind == 2) {
        item.target = [AdvancedMenubarActionTarget shared];
        item.action = @selector(invokeMenuItem:);
        item.representedObject = @(actionValue);
    } else if (targetKind == 3) {
        item.target = [AdvancedMenubarEditMenuBridge shared];
        item.action = @selector(invokeEditMenuItem:);
        item.representedObject = @(actionValue);
    } else if (selectorName.length > 0) {
        item.action = NSSelectorFromString(selectorName);
        item.target = targetKind == 1 ? NSApp : nil;
    }
}

static BOOL ambMenuContainsAction(NSMenu *menu, NSString *actionName) {
    for (NSMenuItem *item in menu.itemArray) {
        if ([ambActionName(item) isEqualToString:actionName]) return YES;
        if (item.submenu != nil && ambMenuContainsAction(item.submenu, actionName)) return YES;
    }
    return NO;
}

static BOOL ambIsAutoFillItem(NSMenuItem *item) {
    return item.submenu != nil &&
           ambMenuContainsAction(item.submenu, @"_handleInsertFromContactsCommand:");
}

static BOOL ambIsDictationItem(NSMenuItem *item) {
    NSString *action = ambActionName(item);
    return [action isEqualToString:@"startDictation:"] ||
           [action isEqualToString:@"startDictationFromMenu:"];
}

static BOOL ambIsWritingToolsItem(NSMenuItem *item) {
    return item.submenu != nil && ambMenuContainsAction(item.submenu, @"showWritingTools:");
}

static BOOL ambIsEmojiItem(NSMenuItem *item) {
    return [ambActionName(item) isEqualToString:@"orderFrontCharacterPalette:"];
}

static void ambRemoveAutomaticEditItems(NSMenu *menu) {
    for (NSMenuItem *item in menu.itemArray.copy) {
        if (ambIsWritingToolsItem(item) || ambIsAutoFillItem(item) ||
            ambIsDictationItem(item) || ambIsEmojiItem(item)) {
            [menu removeItem:item];
        }
    }
    ambCleanupSeparators(menu);
}

static void ambMergeAutomaticEditDuplicates(NSMenu *menu) {
    NSMutableArray<NSMenuItem *> *autoFillItems = [NSMutableArray array];
    NSMutableArray<NSMenuItem *> *dictationItems = [NSMutableArray array];
    for (NSMenuItem *item in menu.itemArray) {
        if (ambIsAutoFillItem(item)) [autoFillItems addObject:item];
        if (ambIsDictationItem(item)) [dictationItems addObject:item];
    }

    if (autoFillItems.count > 1) {
        NSMenuItem *primary = autoFillItems.firstObject;
        for (NSMenuItem *item in autoFillItems) {
            if (primary.image == nil && item.image != nil) primary.image = item.image.copy;
        }
        for (NSUInteger i = 1; i < autoFillItems.count; i++) {
            [menu removeItem:autoFillItems[i]];
        }
    }

    if (dictationItems.count > 1) {
        NSMenuItem *primary = dictationItems.firstObject;
        for (NSMenuItem *item in dictationItems) {
            if (primary.image == nil && item.image != nil) primary.image = item.image.copy;
            if (primary.keyEquivalent.length == 0 && item.keyEquivalent.length > 0) {
                primary.keyEquivalent = item.keyEquivalent;
                primary.keyEquivalentModifierMask = item.keyEquivalentModifierMask;
            }
        }
        for (NSUInteger i = 1; i < dictationItems.count; i++) {
            [menu removeItem:dictationItems[i]];
        }
    }

    if (autoFillItems.count > 1 || dictationItems.count > 1) ambCleanupSeparators(menu);
}

@interface AdvancedMenubarAutomaticEditMenuDelegate : NSObject <NSMenuDelegate>
@property(nonatomic) BOOL suppressAutomaticItems;
@end

@implementation AdvancedMenubarAutomaticEditMenuDelegate
- (void)menuWillOpen:(NSMenu *)menu {
    if (self.suppressAutomaticItems) {
        ambRemoveAutomaticEditItems(menu);
    } else {
        ambMergeAutomaticEditDuplicates(menu);
    }
}
@end

static NSMenuItem *ambMenuItemWithAction(NSMenu *menu, NSString *actionName) {
    for (NSMenuItem *item in menu.itemArray) {
        if ([ambActionName(item) isEqualToString:actionName]) return item;
        NSMenuItem *nested = item.submenu == nil
            ? nil
            : ambMenuItemWithAction(item.submenu, actionName);
        if (nested != nil) return nested;
    }
    return nil;
}

typedef NSMenuItem *(*AmbSystemMenuItemFactory)(id, SEL, NSString *, SEL, NSString *);

static NSMenuItem *ambSystemMenuItem(NSString *title, SEL action, NSString *keyEquivalent) {
    SEL factorySelector = NSSelectorFromString(@"_systemItemWithTitle:action:keyEquivalent:");
    if ([NSMenuItem respondsToSelector:factorySelector]) {
        AmbSystemMenuItemFactory factory =
            (AmbSystemMenuItemFactory)[NSMenuItem methodForSelector:factorySelector];
        if (factory != NULL) {
            return factory(NSMenuItem.class, factorySelector, title, action, keyEquivalent);
        }
    }
    return [[NSMenuItem alloc] initWithTitle:title action:action keyEquivalent:keyEquivalent];
}

static BOOL ambWritingToolsAreAutomaticallyInserted(void) {
    if (![NSMenuItem respondsToSelector:@selector(writingToolsItems)]) return NO;

    NSWindow *probeWindow =
        [[NSWindow alloc] initWithContentRect:NSMakeRect(0, 0, 1, 1)
                                   styleMask:NSWindowStyleMaskBorderless
                                     backing:NSBackingStoreBuffered
                                       defer:NO];
    NSTextView *probeView = [[NSTextView alloc] initWithFrame:probeWindow.contentView.bounds];
    probeView.string = @"Writing Tools";
    probeView.selectedRange = NSMakeRange(0, probeView.string.length);
    [probeWindow.contentView addSubview:probeView];
    [probeWindow makeFirstResponder:probeView];

    NSEvent *event =
        [NSEvent mouseEventWithType:NSEventTypeRightMouseDown
                           location:NSMakePoint(0.5, 0.5)
                      modifierFlags:0
                          timestamp:NSProcessInfo.processInfo.systemUptime
                       windowNumber:probeWindow.windowNumber
                            context:nil
                        eventNumber:0
                         clickCount:1
                           pressure:1.0];
    NSMenu *automaticMenu = [probeView menuForEvent:event];
    return automaticMenu != nil && ambMenuContainsAction(automaticMenu, @"showWritingTools:");
}

static NSArray<NSMenuItem *> *ambAutomaticEditItems(NSMenu *mainMenu) {
    NSMenu *source = nil;
    for (NSMenuItem *topItem in mainMenu.itemArray) {
        NSMenu *submenu = topItem.submenu;
        if (submenu == nil) continue;
        if (ambMenuContainsAction(submenu, @"showWritingTools:") ||
            ambMenuContainsAction(submenu, @"_handleInsertFromContactsCommand:") ||
            ambMenuContainsAction(submenu, @"startDictation:") ||
            ambMenuContainsAction(submenu, @"orderFrontCharacterPalette:")) {
            source = submenu;
            break;
        }
    }

    NSMenuItem *writingToolsItem = nil;
    NSMenuItem *autoFillItem = nil;
    for (NSMenuItem *item in source.itemArray) {
        BOOL writingTools = item.submenu != nil && ambMenuContainsAction(item.submenu, @"showWritingTools:");
        BOOL autoFill = item.submenu != nil && ambMenuContainsAction(item.submenu, @"_handleInsertFromContactsCommand:");
        if (writingTools && writingToolsItem == nil) writingToolsItem = item.copy;
        if (autoFill && autoFillItem == nil) autoFillItem = item.copy;
    }

    if (writingToolsItem == nil && ambWritingToolsAreAutomaticallyInserted()) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability-new"
        writingToolsItem = NSMenuItem.writingToolsItems.firstObject.copy;
#pragma clang diagnostic pop
    }

    NSBundle *appKit = [NSBundle bundleForClass:NSMenuItem.class];
    if (autoFillItem == nil) {
        NSString *title = [appKit localizedStringForKey:@"AutoFill"
                                                  value:@"AutoFill"
                                                  table:@"InputManager"];
        autoFillItem = [[NSMenuItem alloc] initWithTitle:title action:nil keyEquivalent:@""];
        NSMenu *submenu = [[NSMenu alloc] initWithTitle:title];
        SEL appendSelector = NSSelectorFromString(@"_appendSystemAutoFillMenuItemsToMenu:");
        if ([NSApp respondsToSelector:appendSelector]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
            [NSApp performSelector:appendSelector withObject:submenu];
#pragma clang diagnostic pop
        }
        autoFillItem.submenu = submenu;
        autoFillItem.enabled = submenu.numberOfItems > 0;
        if (submenu.numberOfItems == 0) autoFillItem = nil;
    }

    NSMenuItem *dictationItem = [ambMenuItemWithAction(source, @"startDictation:") copy];
    if (dictationItem == nil) {
        NSString *dictationTitle = [appKit localizedStringForKey:@"Start Dictation…"
                                                           value:@"Start Dictation…"
                                                           table:@"DictationManager"];
        dictationItem = ambSystemMenuItem(dictationTitle,
                                          @selector(startDictationFromMenu:), @"🎤");
        dictationItem.target = [AdvancedMenubarSystemEditTarget shared];
        dictationItem.keyEquivalentModifierMask = 0;
        dictationItem.tag = 1735159650;
        dictationItem.image = ambImage(1, @"microphone", YES);
    }

    NSMenuItem *emojiItem = [ambMenuItemWithAction(source, @"orderFrontCharacterPalette:") copy];
    if (emojiItem == nil) {
        NSString *emojiTitle = [appKit localizedStringForKey:@"Emoji & Symbols"
                                                       value:@"Emoji & Symbols"
                                                       table:@"InputManager"];
        emojiItem = ambSystemMenuItem(emojiTitle,
                                      @selector(orderFrontCharacterPalette:), @"");
        emojiItem.target = NSApp;
        SEL globeSelector = NSSelectorFromString(@"_updateCharacterPaletteItemKeyEquivalent:");
        if ([NSApp respondsToSelector:globeSelector]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Warc-performSelector-leaks"
            [NSApp performSelector:globeSelector withObject:emojiItem];
#pragma clang diagnostic pop
        }
    }

    NSMutableArray<NSMenuItem *> *items = [NSMutableArray arrayWithCapacity:4];
    if (writingToolsItem != nil) [items addObject:writingToolsItem];
    if (autoFillItem != nil) [items addObject:autoFillItem];
    [items addObject:dictationItem];
    [items addObject:emojiItem];
    return items.copy;
}

static void ambAppendAutomaticEditItems(NSMenu *editMenu, NSMenu *sourceMainMenu) {
    NSArray<NSMenuItem *> *items = ambAutomaticEditItems(sourceMainMenu);
    if (items.count == 0) return;
    if (editMenu.numberOfItems > 0 && !editMenu.itemArray.lastObject.separatorItem) {
        [editMenu addItem:NSMenuItem.separatorItem];
    }
    for (NSMenuItem *item in items) [editMenu addItem:item.copy];
}

static int64_t ambInstallMenu(const uint8_t *bytes, size_t length) {
    __block int64_t result = 0;
    void (^work)(void) = ^{
        @autoreleasepool {
            AmbReader reader = { bytes, length, 0, YES };
            if (ambReadU32(&reader) != 0x414D4231 || ambReadU32(&reader) != 1) return;
            int32_t count = ambReadI32(&reader);
            if (!reader.valid || count < 0 || count > 100000) return;

            NSMenu *sourceMainMenu = NSApp.mainMenu;
            NSMenu *mainMenu = [[NSMenu alloc] initWithTitle:@"MainMenu"];
            NSMutableArray *menus = [NSMutableArray arrayWithCapacity:(NSUInteger)count];
            for (int32_t i = 0; i < count; i++) [menus addObject:NSNull.null];
            NSMenu *windowMenu = nil;
            NSMenu *helpMenu = nil;
            NSMenu *servicesMenu = nil;
            NSMenu *editMenu = nil;
            BOOL suppressAutomaticEditItems = NO;

            for (int32_t index = 0; index < count && reader.valid; index++) {
                int32_t kind = ambReadI32(&reader);
                int32_t parent = ambReadI32(&reader);
                NSString *title = ambReadString(&reader) ?: @"";
                NSString *selectorName = ambReadString(&reader);
                NSString *keyEquivalent = ambReadString(&reader);
                int32_t modifiers = ambReadI32(&reader);
                BOOL enabled = ambReadBool(&reader);
                int32_t state = ambReadI32(&reader);
                int32_t targetKind = ambReadI32(&reader);
                int64_t actionValue = ambReadI64(&reader);
                int32_t iconKind = ambReadI32(&reader);
                NSString *iconValue = ambReadString(&reader);
                BOOL iconTemplate = ambReadBool(&reader);
                NSString *subtitle = ambReadString(&reader);
                NSString *tooltip = ambReadString(&reader);
                NSString *badge = ambReadString(&reader);
                if (!reader.valid) break;

                if (kind >= 1 && kind <= 6) {
                    NSMenuItem *topItem = [[NSMenuItem alloc] initWithTitle:title action:nil keyEquivalent:@""];
                    topItem.enabled = enabled;
                    NSMenu *submenu = [[NSMenu alloc] initWithTitle:title];
                    submenu.autoenablesItems = NO;
                    topItem.submenu = submenu;
                    [mainMenu addItem:topItem];
                    menus[(NSUInteger)index] = submenu;
                    if (kind == 3) windowMenu = submenu;
                    if (kind == 4) helpMenu = submenu;
                    if (kind == 5 || kind == 6) {
                        editMenu = submenu;
                        suppressAutomaticEditItems = kind == 6;
                    }
                    continue;
                }

                if (parent < 0 || parent >= count || menus[(NSUInteger)parent] == NSNull.null) {
                    reader.valid = NO;
                    break;
                }
                NSMenu *parentMenu = menus[(NSUInteger)parent];

                if (kind == 12) {
                    [parentMenu addItem:NSMenuItem.separatorItem];
                } else if (kind == 13) {
                    NSMenuItem *header;
                    if ([NSMenuItem respondsToSelector:@selector(sectionHeaderWithTitle:)]) {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunguarded-availability-new"
                        header = [NSMenuItem sectionHeaderWithTitle:title];
#pragma clang diagnostic pop
                    } else {
                        header = [[NSMenuItem alloc] initWithTitle:title action:nil keyEquivalent:@""];
                        header.enabled = NO;
                    }
                    [parentMenu addItem:header];
                } else if (kind == 11 || kind == 14) {
                    NSMenuItem *item = [[NSMenuItem alloc] initWithTitle:title action:nil keyEquivalent:@""];
                    NSMenu *submenu = [[NSMenu alloc] initWithTitle:title];
                    submenu.autoenablesItems = NO;
                    item.submenu = submenu;
                    ambConfigureItem(item, nil, nil, 0, enabled, -1, 0, 0,
                                     iconKind, iconValue, iconTemplate, subtitle, tooltip, badge);
                    [parentMenu addItem:item];
                    menus[(NSUInteger)index] = submenu;
                    if (kind == 14) servicesMenu = submenu;
                } else if (kind == 10) {
                    NSMenuItem *item = [[NSMenuItem alloc] initWithTitle:title action:nil keyEquivalent:keyEquivalent ?: @""];
                    ambConfigureItem(item, selectorName, keyEquivalent, modifiers, enabled, state,
                                     targetKind, actionValue, iconKind, iconValue, iconTemplate,
                                     subtitle, tooltip, badge);
                    [parentMenu addItem:item];
                }
            }

            if (!reader.valid) return;
            if (editMenu != nil && !suppressAutomaticEditItems) {
                ambAppendAutomaticEditItems(editMenu, sourceMainMenu);
            }
            if (editMenu != nil) {
                AdvancedMenubarAutomaticEditMenuDelegate *delegate =
                    [[AdvancedMenubarAutomaticEditMenuDelegate alloc] init];
                delegate.suppressAutomaticItems = suppressAutomaticEditItems;
                gAutomaticEditMenuDelegate = delegate;
                editMenu.delegate = delegate;
            }
            NSApp.mainMenu = mainMenu;
            NSApp.servicesMenu = servicesMenu;
            NSApp.windowsMenu = windowMenu;
            NSApp.helpMenu = helpMenu;
            result = (int64_t)(intptr_t)CFBridgingRetain(mainMenu);
        }
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
    return result;
}

JNIEXPORT jlong JNICALL
Java_dev_hansholz_advancedmenubar_NativeMenuBridge_nativeInstallMenu(JNIEnv *env, jclass clazz, jbyteArray payload) {
    (void)clazz;
    if (payload == NULL) return 0;
    jsize length = (*env)->GetArrayLength(env, payload);
    uint8_t *bytes = malloc((size_t)length);
    if (bytes == NULL) return 0;
    (*env)->GetByteArrayRegion(env, payload, 0, length, (jbyte *)bytes);
    int64_t result = ambInstallMenu(bytes, (size_t)length);
    free(bytes);
    return (jlong)result;
}

JNIEXPORT jlong JNICALL
Java_dev_hansholz_advancedmenubar_NativeMenuBridge_nativeRetainMainMenu(JNIEnv *env, jclass clazz) {
    (void)env; (void)clazz;
    __block int64_t result = 0;
    void (^work)(void) = ^{
        AdvancedMenubarMenuState *state = [[AdvancedMenubarMenuState alloc] init];
        state.mainMenu = NSApp.mainMenu;
        state.servicesMenu = NSApp.servicesMenu;
        state.windowsMenu = NSApp.windowsMenu;
        state.helpMenu = NSApp.helpMenu;
        result = (int64_t)(intptr_t)CFBridgingRetain(state);
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
    return (jlong)result;
}

JNIEXPORT void JNICALL
Java_dev_hansholz_advancedmenubar_NativeMenuBridge_nativeRestoreMainMenu(JNIEnv *env, jclass clazz, jlong handle) {
    (void)env; (void)clazz;
    AdvancedMenubarMenuState *state =
        (__bridge AdvancedMenubarMenuState *)(void *)(intptr_t)handle;
    void (^work)(void) = ^{
        NSApp.mainMenu = state.mainMenu;
        NSApp.servicesMenu = state.servicesMenu;
        NSApp.windowsMenu = state.windowsMenu;
        NSApp.helpMenu = state.helpMenu;
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
}

JNIEXPORT void JNICALL
Java_dev_hansholz_advancedmenubar_NativeMenuBridge_nativeRelease(JNIEnv *env, jclass clazz, jlong handle) {
    (void)env; (void)clazz;
    if (handle == 0) return;
    void (^work)(void) = ^{ CFRelease((CFTypeRef)(void *)(intptr_t)handle); };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
}

static NSString *ambNSString(JNIEnv *env, jstring value) {
    if (value == NULL) return nil;
    const char *utf = (*env)->GetStringUTFChars(env, value, NULL);
    if (utf == NULL) return nil;
    NSString *result = [NSString stringWithUTF8String:utf] ?: @"";
    (*env)->ReleaseStringUTFChars(env, value, utf);
    return result;
}

JNIEXPORT void JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeApplyAppearance(
    JNIEnv *env, jclass clazz, jboolean isDark) {
    (void)env; (void)clazz;
    void (^work)(void) = ^{
        NSString *name = isDark ? NSAppearanceNameDarkAqua : NSAppearanceNameAqua;
        NSApp.appearance = [NSAppearance appearanceNamed:name];
    };
    if (NSThread.isMainThread) work(); else dispatch_async(dispatch_get_main_queue(), work);
}

JNIEXPORT jlong JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeCaptureCurrentEvent(
    JNIEnv *env, jclass clazz) {
    (void)env; (void)clazz;
    __block int64_t result = 0;
    void (^work)(void) = ^{
        if (gTextContextMenuScheduledOrTracking) return;
        gCapturedContextEvent = NSApp.currentEvent.copy;
        result = gCapturedContextEvent == nil ? 0 : 1;
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
    return (jlong)result;
}

JNIEXPORT void JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeShowTextContextMenu(
    JNIEnv *env,
    jclass clazz,
    jstring selectedText,
    jboolean isEditable,
    jlong eventAddress,
    jdouble contentHeight,
    jdouble contentWidth,
    jdouble left,
    jdouble top,
    jdouble right,
    jdouble bottom,
    jobjectArray labelsArray,
    jobjectArray symbolsArray,
    jbooleanArray enabledArray,
    jlongArray actionIdsArray,
    jint customActionCount,
    jboolean showExtraOptions,
    jlong textCallbackId) {
    (void)clazz; (void)eventAddress;
    NSString *selected = ambNSString(env, selectedText) ?: @"";
    jsize count = labelsArray == NULL ? 0 : (*env)->GetArrayLength(env, labelsArray);
    NSMutableArray<NSString *> *labels = [NSMutableArray arrayWithCapacity:(NSUInteger)count];
    NSMutableArray *symbols = [NSMutableArray arrayWithCapacity:(NSUInteger)count];
    for (jsize i = 0; i < count; i++) {
        jstring label = (jstring)(*env)->GetObjectArrayElement(env, labelsArray, i);
        [labels addObject:ambNSString(env, label) ?: @""];
        if (label != NULL) (*env)->DeleteLocalRef(env, label);
        jstring symbol = symbolsArray == NULL ? NULL : (jstring)(*env)->GetObjectArrayElement(env, symbolsArray, i);
        [symbols addObject:ambNSString(env, symbol) ?: NSNull.null];
        if (symbol != NULL) (*env)->DeleteLocalRef(env, symbol);
    }
    NSMutableArray<NSNumber *> *enabled = [NSMutableArray arrayWithCapacity:(NSUInteger)count];
    NSMutableArray<NSNumber *> *actionIds = [NSMutableArray arrayWithCapacity:(NSUInteger)count];
    if (count > 0) {
        jboolean *enabledValues = (*env)->GetBooleanArrayElements(env, enabledArray, NULL);
        jlong *idValues = (*env)->GetLongArrayElements(env, actionIdsArray, NULL);
        for (jsize i = 0; i < count; i++) {
            [enabled addObject:@(enabledValues[i] != JNI_FALSE)];
            [actionIds addObject:@((int64_t)idValues[i])];
        }
        (*env)->ReleaseBooleanArrayElements(env, enabledArray, enabledValues, JNI_ABORT);
        (*env)->ReleaseLongArrayElements(env, actionIdsArray, idValues, JNI_ABORT);
    }

    void (^work)(void) = ^{
        @autoreleasepool {
            NSTextView *previousAnchor = gRetainedTextContextView;
            gRetainedTextContextView = nil;
            gRetainedTextContextMenu = nil;
            gRetainedTextContextDelegate = nil;
            previousAnchor.delegate = nil;
            [previousAnchor removeFromSuperview];

            NSWindow *window = NSApp.keyWindow;
            NSView *contentView = window.contentView;
            if (window == nil || contentView == nil) {
                gTextContextMenuScheduledOrTracking = NO;
                return;
            }

            double rawY = contentHeight - bottom;
            double fieldX = MAX(0.0, MIN(left, MAX(0.0, contentWidth - 1.0)));
            double fieldY = MAX(0.0, MIN(rawY, MAX(0.0, contentHeight - 1.0)));
            double fieldW = MAX(1.0, MIN(MAX(1.0, right - left) - (fieldX - left), contentWidth - fieldX));
            double fieldH = MAX(1.0, MIN(MAX(1.0, bottom - top) - (fieldY - rawY), contentHeight - fieldY));

            AdvancedMenubarNoHitTextView *textView = ambCreateHiddenTextView(isEditable != JNI_FALSE);
            textView.string = selected;
            textView.selectedRange = NSMakeRange(0, selected.length);
            textView.frame = NSMakeRect(fieldX, fieldY, fieldW, fieldH);
            ambConfigureHiddenTextView(textView);
            [contentView addSubview:textView];

            NSResponder *previousResponder = window.firstResponder;
            [window makeFirstResponder:textView];
            NSEvent *event = gCapturedContextEvent ?: NSApp.currentEvent;
            gCapturedContextEvent = nil;
            NSMenu *menu = [textView menuForEvent:event];
            if (menu == nil) {
                [window makeFirstResponder:previousResponder];
                [textView removeFromSuperview];
                gTextContextMenuScheduledOrTracking = NO;
                return;
            }

            ambDebugMenu(menu, @"context-before-filter");

            if (showExtraOptions) ambRemoveExcludedTextItems(menu); else ambKeepBasicTextItems(menu);
            if (isEditable != JNI_FALSE && selected.length == 0) {
                ambRemoveSelectionOnlyTextItems(menu);
            } else if (isEditable == JNI_FALSE) {
                ambRemoveReadOnlyEditingItems(menu);
            }
            ambDebugMenu(menu, @"context-after-filter");
            NSInteger insertion = ambTextInsertIndex(menu);
            NSInteger insertedCount = 0;
            for (NSInteger i = 0; i < count; i++) {
                NSInteger customStart = count - customActionCount;
                if (i == customStart && insertion + insertedCount > 0) {
                    [menu insertItem:NSMenuItem.separatorItem
                             atIndex:MIN(insertion + insertedCount, menu.numberOfItems)];
                    insertedCount++;
                }
                NSString *symbol = symbols[(NSUInteger)i] == NSNull.null ? nil : symbols[(NSUInteger)i];
                NSMenuItem *item = ambTextCallbackItem(labels[(NSUInteger)i], symbol,
                                                       enabled[(NSUInteger)i].boolValue,
                                                       actionIds[(NSUInteger)i].longLongValue);
                [menu insertItem:item atIndex:MIN(insertion + insertedCount, menu.numberOfItems)];
                insertedCount++;
            }
            if (count > 0 && (count == 1 || menu.numberOfItems > insertion + count)) {
                NSInteger separatorIndex = MIN(insertion + insertedCount, menu.numberOfItems);
                [menu insertItem:NSMenuItem.separatorItem atIndex:separatorIndex];
            }
            ambCleanupSeparators(menu);

            AdvancedMenubarTextDelegate *textDelegate = nil;
            if (textCallbackId != 0) {
                textDelegate = [[AdvancedMenubarTextDelegate alloc] init];
                textDelegate.callbackId = textCallbackId;
                textDelegate.originalText = textView.string;
                textView.delegate = textDelegate;
            }
            if (menu.numberOfItems > 0 && event != nil) {
                [NSMenu popUpContextMenu:menu withEvent:event forView:textView];
            }

            NSResponder *restoreResponder = previousResponder;
            if (restoreResponder == nil || restoreResponder == textView) {
                restoreResponder = contentView;
            }
            if (![window makeFirstResponder:restoreResponder]) {
                [window makeFirstResponder:contentView];
            }

            NSString *changedText = textDelegate.changedText.copy;
            int64_t changedTextCallbackId = textDelegate.callbackId;

            // Retain a one-point anchor for asynchronous AppKit popovers.
            textView.frame = NSMakeRect(fieldX, fieldY + fieldH / 2.0, fieldW, 1.0);
            ambConfigureHiddenTextView(textView);
            gRetainedTextContextView = textView;
            gRetainedTextContextMenu = menu;
            gRetainedTextContextDelegate = textDelegate;
            gTextContextMenuScheduledOrTracking = NO;
            ambWakeTaoEventLoop();

            [contentView setNeedsDisplay:YES];
            if (changedText != nil) {
                // Updating a legacy BasicTextField can synchronously recompose and restart its
                // input session. Wait until AppKit has completely unwound menu tracking and the
                // temporary first responder has been restored before entering Compose again.
                dispatch_async(dispatch_get_main_queue(), ^{
                    ambReportTextChange(changedTextCallbackId, changedText);
                    ambWakeTaoEventLoop();
                });
            }
            JNIEnv *callbackEnv = ambEnv();
            if (callbackEnv != NULL && gTextBridgeClass != NULL && gTextOnDismissed != NULL) {
                (*callbackEnv)->CallStaticVoidMethod(callbackEnv, gTextBridgeClass, gTextOnDismissed);
                if ((*callbackEnv)->ExceptionCheck(callbackEnv)) {
                    (*callbackEnv)->ExceptionDescribe(callbackEnv);
                    (*callbackEnv)->ExceptionClear(callbackEnv);
                }
            }
        }
    };
    // Let Tao receive rightMouseUp before AppKit starts modal menu tracking.
    void (^schedule)(void) = ^{
        if (gTextContextMenuScheduledOrTracking) return;
        gTextContextMenuScheduledOrTracking = YES;
        const NSUInteger rightButtonMask = 1UL << 1;
        if ((NSEvent.pressedMouseButtons & rightButtonMask) != 0) {
            gTextContextMouseUpMonitor = [NSEvent addLocalMonitorForEventsMatchingMask:NSEventMaskRightMouseUp
                handler:^NSEvent *(NSEvent *event) {
                    if (gTextContextMouseUpMonitor != nil) {
                        [NSEvent removeMonitor:gTextContextMouseUpMonitor];
                        gTextContextMouseUpMonitor = nil;
                    }
                    dispatch_async(dispatch_get_main_queue(), work);
                    return event;
                }];
        } else {
            dispatch_async(dispatch_get_main_queue(), work);
        }
    };
    if (NSThread.isMainThread) schedule(); else dispatch_async(dispatch_get_main_queue(), schedule);
}

JNIEXPORT jlong JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeSnapshotClipboard(
    JNIEnv *env, jclass clazz) {
    (void)env; (void)clazz;
    __block int64_t result = 0;
    void (^work)(void) = ^{
        NSMutableArray<NSDictionary<NSPasteboardType, NSData *> *> *snapshot = [NSMutableArray array];
        for (NSPasteboardItem *item in NSPasteboard.generalPasteboard.pasteboardItems) {
            NSMutableDictionary<NSPasteboardType, NSData *> *values = [NSMutableDictionary dictionary];
            for (NSPasteboardType type in item.types) {
                NSData *data = [item dataForType:type];
                if (data != nil) values[type] = data;
            }
            [snapshot addObject:values.copy];
        }
        result = (int64_t)(intptr_t)CFBridgingRetain(snapshot.copy);
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
    return (jlong)result;
}

JNIEXPORT jlong JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeSetClipboardString(
    JNIEnv *env, jclass clazz, jstring value) {
    (void)clazz;
    NSString *text = ambNSString(env, value) ?: @"";
    __block NSInteger changeCount = 0;
    void (^work)(void) = ^{
        [NSPasteboard.generalPasteboard clearContents];
        [NSPasteboard.generalPasteboard setString:text forType:NSPasteboardTypeString];
        changeCount = NSPasteboard.generalPasteboard.changeCount;
    };
    if (NSThread.isMainThread) work(); else dispatch_sync(dispatch_get_main_queue(), work);
    return (jlong)changeCount;
}

JNIEXPORT void JNICALL
Java_dev_hansholz_advancedmenubar_NativeTextContextMenuBridge_nativeRestoreClipboardLater(
    JNIEnv *env, jclass clazz, jlong snapshotHandle, jlong expectedChangeCount) {
    (void)env; (void)clazz;
    if (snapshotHandle == 0) return;
    NSArray<NSDictionary<NSPasteboardType, NSData *> *> *snapshot =
        (__bridge NSArray<NSDictionary<NSPasteboardType, NSData *> *> *)(void *)(intptr_t)snapshotHandle;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(100 * NSEC_PER_MSEC)), dispatch_get_main_queue(), ^{
        NSPasteboard *pasteboard = NSPasteboard.generalPasteboard;
        if (pasteboard.changeCount == (NSInteger)expectedChangeCount) {
            [pasteboard clearContents];
            NSMutableArray<NSPasteboardItem *> *items = [NSMutableArray arrayWithCapacity:snapshot.count];
            for (NSDictionary<NSPasteboardType, NSData *> *values in snapshot) {
                NSPasteboardItem *item = [[NSPasteboardItem alloc] init];
                for (NSPasteboardType type in values) {
                    [item setData:values[type] forType:type];
                }
                [items addObject:item];
            }
            if (items.count > 0) [pasteboard writeObjects:items];
        }
        CFRelease((CFTypeRef)(void *)(intptr_t)snapshotHandle);
    });
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    (void)reserved;
    gJavaVm = vm;
    JNIEnv *env = NULL;
    if ((*vm)->GetEnv(vm, (void **)&env, JNI_VERSION_1_6) != JNI_OK) return JNI_ERR;
    jclass local = (*env)->FindClass(env, "dev/hansholz/advancedmenubar/NativeMenuBridge");
    if (local == NULL) return JNI_ERR;
    gBridgeClass = (*env)->NewGlobalRef(env, local);
    (*env)->DeleteLocalRef(env, local);
    gOnAction = (*env)->GetStaticMethodID(env, gBridgeClass, "onAction", "(J)V");
    jclass textLocal = (*env)->FindClass(env, "dev/hansholz/advancedmenubar/NativeTextContextMenuBridge");
    if (textLocal == NULL) return JNI_ERR;
    gTextBridgeClass = (*env)->NewGlobalRef(env, textLocal);
    (*env)->DeleteLocalRef(env, textLocal);
    gTextOnAction = (*env)->GetStaticMethodID(env, gTextBridgeClass, "onAction", "(J)V");
    gTextOnChanged = (*env)->GetStaticMethodID(env, gTextBridgeClass, "onTextChanged", "(JLjava/lang/String;)V");
    gTextOnDismissed = (*env)->GetStaticMethodID(env, gTextBridgeClass, "onDismissed", "()V");

    jclass taoLocal = (*env)->FindClass(env, "dev/nucleusframework/window/tao/ffi/NativeTaoBridge");
    if (taoLocal != NULL) {
        gTaoBridgeClass = (*env)->NewGlobalRef(env, taoLocal);
        (*env)->DeleteLocalRef(env, taoLocal);
        gTaoWake = (*env)->GetStaticMethodID(env, gTaoBridgeClass, "nativeWake", "()V");
        if ((*env)->ExceptionCheck(env)) {
            (*env)->ExceptionClear(env);
            gTaoWake = NULL;
        }
    } else if ((*env)->ExceptionCheck(env)) {
        (*env)->ExceptionClear(env);
    }
    return gOnAction == NULL || gTextOnAction == NULL || gTextOnChanged == NULL ||
           gTextOnDismissed == NULL ? JNI_ERR : JNI_VERSION_1_6;
}
