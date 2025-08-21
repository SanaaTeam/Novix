package com.sanaa.designsystem.design_system.component.modifiers

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


internal fun Modifier.innerShadow(
    shape: Shape,
    color: Color = Color.Black,
    blur: Dp,
    offsetX: Dp = 2.dp,
    offsetY: Dp = 2.dp,
    spread: Dp = 0.dp,
) = this.drawWithContent {
    drawContent()

    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)

    val paint = Paint()
    drawIntoCanvas { canvas ->
        paint.color = color
        canvas.saveLayer(size.toRect(), paint)
        canvas.drawOutline(shadowOutline, paint)

        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blur.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }

        paint.color = Color.Black
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

internal fun Modifier.dropShadow(
    shape: Shape = CircleShape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
    scaleX: Float = 1f,
    scaleY: Float = 1f,
) = this.drawBehind {
    val scaleXHat = (size.width + spread.toPx()) * (scaleX - 1)
    val scaleYHat = (size.height + spread.toPx()) * (scaleY - 1)

    val shadowSize =
        Size(size.width + spread.toPx() + scaleXHat, size.height + spread.toPx() + scaleYHat)
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
    val paint = Paint()
    paint.color = color

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }
    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx() - scaleXHat / 2, offsetY.toPx() - scaleYHat / 2)
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}