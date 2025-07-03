package com.sanaa.inappropriate_image_viewer_library.component

import android.os.Build
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sanaa.inappropriate_image_viewer_library.classifier.TfLiteImageClassifier

@Composable
fun SafeImageViewer(
    imageUrl: String,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 20.dp,
    blurredEdgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Rectangle,
    overlayColor: Color = Color(0xAA000000),
    sfwThreshold: Float = 0.5f,
    nsfwThreshold: Float = 0.5f,
) {
    val context = mutableStateOf(LocalContext.current)
    val classifier by remember { mutableStateOf(TfLiteImageClassifier(context.value)) }

    var blurImage by remember { mutableStateOf(true) }
    val isAndroid12OrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .allowHardware(false)
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
                val bitmap = success.result.drawable.toBitmap()
                blurImage = classifier.isInappropriateImage(
                    bitmap = bitmap,
                    sfwThreshold = sfwThreshold,
                    nsfwThreshold = nsfwThreshold
                )

            },
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