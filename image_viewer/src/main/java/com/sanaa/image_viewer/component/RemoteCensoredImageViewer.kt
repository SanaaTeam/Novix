package com.sanaa.image_viewer.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import coil.request.ImageRequest
import com.sanaa.image_viewer.classifier.TfLiteImageClassifier

@Composable
fun RemoteCensoredImageViewer(
    imageUrl: String,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 20.dp,
    blurredEdgeTreatment: BlurredEdgeTreatment = BlurredEdgeTreatment.Rectangle,
    sfwThreshold: Float = 0.5f,
    nsfwThreshold: Float = 0.5f,
    contentDescription: String? = null,
    placeholder: Painter? = null,
    error: Painter? = null,
    fallback: Painter? = error,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate
) {
    val context = LocalContext.current
    val classifier = remember { TfLiteImageClassifier(context) }

    var blurImage by remember { mutableStateOf(true) }

    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build(),
            modifier = modifier.then(
                if (blurImage) {
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
                if (onSuccess != null)
                    onSuccess(success)
            },
            contentDescription = contentDescription,
            placeholder = placeholder,
            error = error,
            fallback = fallback,
            onLoading = onLoading,
            onError = onError,
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
            modelEqualityDelegate = modelEqualityDelegate
        )
    }
}