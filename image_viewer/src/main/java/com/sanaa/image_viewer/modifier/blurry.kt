package com.sanaa.image_viewer.modifier

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

fun Modifier.blurry(
    isBlurForced: Boolean,
    isImageSafe: Boolean,
    blurRadius: Dp,
    isBlurEnabled: Boolean,
    bitmap: Bitmap?,
    scaleFactor: Int = 100
): Modifier {
    return when {
        isBlurForced || (!isImageSafe && isBlurEnabled) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                this.blur(blurRadius)
            } else {
                if (bitmap != null) this.fastBlur(bitmap, scaleFactor = scaleFactor)
                else
                    this.fallBackOverlay()
            }
        }

        else -> this
    }
}

@Stable
fun Modifier.fastBlur(
    sourceBitmap: Bitmap,
    scaleFactor: Int = 8
): Modifier = composed {
    var blurredBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(sourceBitmap, scaleFactor) {
        withContext(Dispatchers.IO) {
            blurredBitmap = fastBlurBitmap(sourceBitmap, scaleFactor).asImageBitmap()
        }
    }

    if (blurredBitmap != null) {
        this.then(
            Modifier.drawWithContent {
                drawIntoCanvas {
                    drawImage(
                        image = blurredBitmap!!,
                        dstSize = IntSize(size.width.toInt(), size.height.toInt())
                    )
                }
            }
        )
    } else {
        this
    }
}

private fun fastBlurBitmap(src: Bitmap, scaleFactor: Int = 8): Bitmap {
    val width = src.width
    val height = src.height

    val smallBitmap = src.scale(width / scaleFactor, height / scaleFactor)

    return smallBitmap.scale(width, height)
}

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.fallBackOverlay(
    overlayAlpha: Float = 1f,
    gradientColors: List<Color> = listOf(
        Color.White.copy(alpha = 0.85f),
        Color.White.copy(alpha = 0.9f)
    ),
    vignetteColor: Color = Color.Black.copy(alpha = 0.9f),

    ): Modifier = composed {

    drawWithContent {
        drawContent()

        val w = size.width
        val h = size.height

        val shapePath = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, w, h),
                )
            )
        }

        // Linear gradient overlay
        val linearBrush = Brush.linearGradient(
            colors = gradientColors,
            start = Offset(0f, 0f),
            end = Offset(w, h)
        )
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.clipPath(shapePath)
            drawRect(
                brush = linearBrush,
                alpha = overlayAlpha,
                topLeft = Offset.Zero,
                size = Size(w, h)
            )
            canvas.restore()
        }

        // Radial vignette
        val vignetteRadius = min(w, h) * 0.9f
        val radialBrush = Brush.radialGradient(
            colors = listOf(Color.Transparent, vignetteColor),
            center = Offset(w * 0.5f, h * 0.5f),
            radius = vignetteRadius
        )
        drawIntoCanvas { canvas ->
            canvas.save()
            canvas.clipPath(shapePath)
            drawRect(brush = radialBrush)
            canvas.restore()
        }
    }
}