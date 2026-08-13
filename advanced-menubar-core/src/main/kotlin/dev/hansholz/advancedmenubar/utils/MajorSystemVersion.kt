package dev.hansholz.advancedmenubar.utils

val majorSystemVersion
    get() = System.getProperty("os.version").substringBefore('.').toIntOrNull() ?: 0
