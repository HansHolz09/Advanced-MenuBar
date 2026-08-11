import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val TipsIcon: ImageVector =
    ImageVector
        .Builder(
            name = "Tips",
            defaultWidth = 50.dp,
            defaultHeight = 74.dp,
            viewportWidth = 50f,
            viewportHeight = 74f,
        ).group(
            scaleX = 0.9f,
            scaleY = 0.9f,
            pivotX = 55f,
            pivotY = 82.5f,
        ) {
            path(fill = SolidColor(Color.Black)) {
                moveTo(25f, 48.7f)
                curveTo(25.9f, 48.7f, 26.7f, 47.9f, 26.7f, 46.9f)
                lineTo(26.7f, 33.8f)
                curveTo(26.7f, 33.2f, 27f, 32.9f, 27.6f, 32.9f)
                lineTo(32.9f, 32.9f)
                curveTo(33.8f, 32.9f, 34.6f, 32.1f, 34.6f, 31.1f)
                curveTo(34.6f, 30.1f, 33.8f, 29.2f, 32.9f, 29.2f)
                lineTo(17.1f, 29.2f)
                curveTo(16.2f, 29.2f, 15.4f, 30.1f, 15.4f, 31.1f)
                curveTo(15.4f, 32.1f, 16.2f, 32.9f, 17.1f, 32.9f)
                lineTo(22.4f, 32.9f)
                curveTo(23f, 32.9f, 23.3f, 33.2f, 23.3f, 33.8f)
                lineTo(23.3f, 46.9f)
                curveTo(23.3f, 47.9f, 24.1f, 48.7f, 25f, 48.7f)
                close()
                moveTo(19.1f, 58.7f)
                lineTo(30.9f, 58.7f)
                curveTo(33.1f, 58.7f, 34.4f, 57.1f, 34.4f, 54.5f)
                lineTo(34.4f, 52.6f)
                curveTo(34.4f, 46.2f, 47.3f, 40f, 47.3f, 24.7f)
                curveTo(47.3f, 11.2f, 38.4f, 2f, 25f, 2f)
                curveTo(11.6f, 2f, 2.7f, 11.2f, 2.7f, 24.7f)
                curveTo(2.7f, 40f, 15.6f, 46.2f, 15.6f, 52.6f)
                lineTo(15.6f, 54.5f)
                curveTo(15.6f, 57.1f, 16.9f, 58.7f, 19.1f, 58.7f)
                close()
                moveTo(21.1f, 53.6f)
                curveTo(20.6f, 53.6f, 20.4f, 53.3f, 20.4f, 52.8f)
                lineTo(20.4f, 52.6f)
                curveTo(20.4f, 47.6f, 17.3f, 44f, 14.5f, 40.6f)
                curveTo(10.9f, 36.1f, 7.5f, 31.7f, 7.5f, 24.7f)
                curveTo(7.5f, 14.3f, 14.5f, 7.1f, 25f, 7.1f)
                curveTo(35.5f, 7.1f, 42.5f, 14.3f, 42.5f, 24.7f)
                curveTo(42.5f, 31.7f, 39.1f, 36.2f, 35.4f, 40.6f)
                curveTo(32.6f, 44f, 29.6f, 47.6f, 29.6f, 52.6f)
                lineTo(29.6f, 52.8f)
                curveTo(29.6f, 53.3f, 29.4f, 53.6f, 28.9f, 53.6f)
                close()
                moveTo(21.2f, 72f)
                lineTo(28.8f, 72f)
                curveTo(30f, 72f, 31f, 70.9f, 31f, 69.6f)
                curveTo(31f, 68.3f, 30f, 67.2f, 28.8f, 67.2f)
                lineTo(21.2f, 67.2f)
                curveTo(20f, 67.2f, 19f, 68.3f, 19f, 69.6f)
                curveTo(19f, 70.9f, 20f, 72f, 21.2f, 72f)
                close()
                moveTo(19.4f, 65.3f)
                lineTo(30.6f, 65.3f)
                curveTo(31.8f, 65.3f, 32.8f, 64.2f, 32.8f, 63f)
                curveTo(32.8f, 61.7f, 31.8f, 60.6f, 30.6f, 60.6f)
                lineTo(19.4f, 60.6f)
                curveTo(18.2f, 60.6f, 17.2f, 61.7f, 17.2f, 63f)
                curveTo(17.2f, 64.2f, 18.2f, 65.3f, 19.4f, 65.3f)
                close()
            }
        }.build()
