#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
RESOURCE_DIR="$SCRIPT_DIR/../../resources/advanced-menubar/native"
JAVA_HOME_VALUE="${JAVA_HOME:-$(/usr/libexec/java_home)}"
SOURCE="$SCRIPT_DIR/advanced_menubar.m"

build_arch() {
    local arch="$1"
    local resource_arch="$2"
    local output_dir="$RESOURCE_DIR/darwin-$resource_arch"
    mkdir -p "$output_dir"
    xcrun clang \
        -arch "$arch" \
        -mmacosx-version-min=11.0 \
        -Oz \
        -fobjc-arc \
        -fvisibility=hidden \
        -dynamiclib \
        -nostdlib \
        -Wl,-dead_strip \
        -Wl,-x \
        -Wl,-install_name,@rpath/libadvanced_menubar.dylib \
        -I"$JAVA_HOME_VALUE/include" \
        -I"$JAVA_HOME_VALUE/include/darwin" \
        -framework Cocoa \
        -lobjc \
        -lSystem \
        "$SOURCE" \
        -o "$output_dir/libadvanced_menubar.dylib"
}

build_arch arm64 aarch64
build_arch x86_64 x64
