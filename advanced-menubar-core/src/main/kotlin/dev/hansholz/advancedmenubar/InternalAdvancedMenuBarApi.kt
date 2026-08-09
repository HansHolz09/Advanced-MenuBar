package dev.hansholz.advancedmenubar

/**
 * Marks backend integration APIs that are public only because Advanced Menubar is split into
 * separately published core and renderer artifacts.
 *
 * Application code should use [MenuBarScope] and a window-backend menu function instead.
 */
@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is reserved for Advanced Menubar backend integrations.",
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class InternalAdvancedMenuBarApi
