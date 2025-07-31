package com.sanaa.image_viewer.modifier

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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.core.graphics.scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


fun Modifier.blurry(
    isBlurForced: Boolean,
    isImageSafe: Boolean,
    blurRadius: Dp,
    isBlurEnabled: Boolean,
    bitmap: Bitmap,
    scaleFactor: Int = 100
): Modifier {
    return when {
        isBlurForced || (!isImageSafe && isBlurEnabled) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                this.blur(blurRadius)
            } else {
                this.fastBlur(bitmap, scaleFactor = scaleFactor)
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