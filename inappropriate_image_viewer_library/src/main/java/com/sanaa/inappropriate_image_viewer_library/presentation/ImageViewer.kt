package com.sanaa.inappropriate_image_viewer_library.presentation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sanaa.inappropriate_image_viewer_library.data.TfLiteImageClassifier

@Composable
fun ImageViewer(
    imageUrl: String,
    context: Context,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 20.dp,
    blurredEdgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Rectangle,
    overlayColor: Color = Color(0xAA000000),
    sfwThreshold: Float = 0.5f,
    nsfwThreshold: Float = 0.5f,
) {
    val classifier = TfLiteImageClassifier(context)

    var blurImage by remember { mutableStateOf(true) }
    val isAndroid12OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Needed to get Bitmap
                .build(),
            contentDescription = null,
            modifier = modifier.then(
                if (blurImage && isAndroid12OrAbove) {
                    Modifier.blur(radius = blurRadius, edgeTreatment = blurredEdgeTreatment)
                } else {
                    Modifier
                }
            ),
            onSuccess = { success ->
                val bitmap = success.result.drawable.toBitmap().scale(224, 224)
                blurImage = classifier.isInappropriateImage(
                    bitmap = bitmap,
                    sfwThreshold = sfwThreshold,
                    nsfwThreshold = nsfwThreshold
                )
            },
            onError = {
                blurImage = true
            }
        )
        if (blurImage && !isAndroid12OrAbove) {
            Box(
                modifier = modifier
                    .matchParentSize()
                    .background(overlayColor)
            )
        }
    }
}