package com.sanaa.designsystem.design_system.component.novix_scaffold

import android.graphics.BlurMaskFilter
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.withSave
import com.sanaa.designsystem.design_system.theme.Theme
import kotlin.math.sqrt

@Composable
fun BackgroundShapes(modifier: Modifier = Modifier) {
    Box(modifier) {
        GlowingEquilateralTriangle(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.primary.copy(alpha = 0.08f),
            offsetYFraction = -0.10f,
            offsetXFraction = -0.35f,
            sideFraction = 1.025f,
            blurRadius = 150f
        )

        GlowingEquilateralTriangle(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.secondary.copy(alpha = 0.05f),
            offsetXFraction = 0.50f,
            offsetYFraction = 0.15f,
            rotationDegrees = 180f,
            sideFraction = 1.025f,
            blurRadius = 150f

        )

        GlowingEquilateralTriangle(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.primary.copy(alpha = 0.02f),
            offsetXFraction = -0.35f,
            offsetYFraction = 0.5f,
            sideFraction = 1.025f,
            blurRadius = 150f
        )

        GlowingEquilateralTriangle(
            modifier = Modifier.fillMaxSize(),
            color = Theme.colors.primary.copy(alpha = 0.01f),
            blurRadius = 150f,
            sideFraction = 1.025f,
            offsetXFraction = 0.50f,
            offsetYFraction = 0.75f,
        )
    }
}


@Composable
internal fun GlowingEquilateralTriangle(
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.primary.copy(alpha = 0.07f),
    blurRadius: Float = 600f,
    sideFraction: Float = 0.6f,
    offsetXFraction: Float = 0f,
    offsetYFraction: Float = 0f,
    rotationDegrees: Float = 180f
) {
    Canvas(modifier = modifier) {
        val side = size.width * sideFraction
        val height = (sqrt(3f) / 2f) * side

        val offsetX = size.width * offsetXFraction
        val offsetY = size.height * offsetYFraction

        val centerX = offsetX + side / 2f
        val centerY = offsetY + height / 2f

        val paint = Paint().apply {
            this.color = color.toArgb()
            this.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            this.isAntiAlias = true
        }

        drawIntoCanvas { canvas ->
            canvas.withSave {
                canvas.nativeCanvas.translate(centerX, centerY)
                canvas.nativeCanvas.rotate(rotationDegrees)
                canvas.nativeCanvas.translate(-centerX, -centerY)

                val path = Path().apply {
                    moveTo(offsetX + side / 2f, offsetY)
                    lineTo(offsetX + side, offsetY + height)
                    lineTo(offsetX, offsetY + height)
                    close()
                }

                canvas.nativeCanvas.drawPath(path.asAndroidPath(), paint)
            }
        }
    }
}
