package dev.hansholz.advancedmenubar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

/** Describes an icon that can be rendered by a menu backend. */
sealed interface MenuIcon {
    /** A native SF Symbol. Unsupported renderers omit the icon. */
    data class SFSymbol(
        /** The SF Symbols name, for example `doc.on.doc`. */
        val name: String,
        /** Whether AppKit should tint the image as a template image. */
        val template: Boolean = true,
    ) : MenuIcon

    /** A PNG image kept in memory. */
    data class Png(
        /** The complete encoded PNG data. */
        val bytes: ByteArray,
        /** Whether AppKit should tint the image as a template image. */
        val template: Boolean = true,
    ) : MenuIcon {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Png

            if (template != other.template) return false
            if (!bytes.contentEquals(other.bytes)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = template.hashCode()
            result = 31 * result + bytes.contentHashCode()
            return result
        }
    }

    /** An image loaded from an absolute or application-resolved file-system path. */
    data class File(
        /** Path passed to the native or Swing image loader. */
        val path: String,
        /** Whether AppKit should tint the image as a template image. */
        val template: Boolean = true,
    ) : MenuIcon
}

/**
 * Rasterizes [imageVector] into a high-resolution PNG sized like a native SF Symbol menu icon.
 *
 * Auto-mirrored vectors follow the current layout direction. Remember the result at the call site
 * by calling this function from composition rather than recreating PNG data manually.
 *
 * @param template whether AppKit should tint the resulting image as a template image.
 */
@Composable
fun rememberMenuIconFrom(
    imageVector: ImageVector,
    template: Boolean = true,
): MenuIcon {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val rasterDensity = Density(density.density.coerceAtLeast(2f), density.fontScale)
    val px = with(rasterDensity) { 16.dp.roundToPx().coerceAtLeast(1) }

    val painter = rememberVectorPainter(imageVector)
    val bytes =
        remember(imageVector, px, layoutDirection) {
            val ib = ImageBitmap(px, px)
            val canvas = Canvas(ib)
            val drawScope = CanvasDrawScope()

            drawScope.draw(
                density = rasterDensity,
                layoutDirection = layoutDirection,
                canvas = canvas,
                size = Size(px.toFloat(), px.toFloat()),
            ) {
                with(painter) { draw(Size(px.toFloat(), px.toFloat())) }
            }

            Image
                .makeFromBitmap(ib.asSkiaBitmap())
                .encodeToData(EncodedImageFormat.PNG)
                ?.bytes
                ?: byteArrayOf()
        }

    return MenuIcon.Png(bytes, template)
}
