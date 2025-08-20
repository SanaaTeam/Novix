package com.sanaa.presentation.component

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.sanaa.designsystem.design_system.theme.Theme
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas

@Composable
fun CircleShapeBlur(
    modifier: Modifier = Modifier,
    color: Color = Theme.colors.primary.copy(alpha = 0.20f),
    blurRadius: Float = 2000f
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val paint = android.graphics.Paint().apply {
            this.color = color.toArgb()
            maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        }

        drawIntoCanvas {
            val radius = minOf(size.width, size.height) / 2f
            val centerX = size.width / 2f
            val centerY = size.height / 2f

            it.nativeCanvas.drawCircle(
                centerX, centerY, radius, paint
            )
        }
    }
}