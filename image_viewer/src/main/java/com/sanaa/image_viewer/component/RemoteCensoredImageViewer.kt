package com.sanaa.image_viewer.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import coil.request.ImageRequest
import com.sanaa.image_viewer.classifier.TfLiteImageClassifier
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.launch

@Composable
fun RemoteCensoredImageViewer(
    imageUrl: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    placeholderBackgroundColor: Color = Color(0xFFFFFFFF),
    error: Painter? = null,
    onLoading: ((AsyncImagePainter.State.Loading) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter.State.Success) -> Unit)? = null,
    onError: ((AsyncImagePainter.State.Error) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality,
    clipToBounds: Boolean = true,
    modelEqualityDelegate: EqualityDelegate = DefaultModelEqualityDelegate,
    blurRadius: Int = 20,
    @FloatRange(from = 0.0, to = 1.0) sfwThreshold: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) nsfwThreshold: Float = 0.5f,
) {
    val context = LocalContext.current
    val classifier = remember { TfLiteImageClassifier(context) }

    var blurImage by rememberSaveable { mutableStateOf(true) }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build(),
            modifier = modifier.cloudy(radius = blurRadius, enabled = blurImage),
            onSuccess = { success ->
                coroutineScope.launch {
                    val bitmap = success.result.drawable.toBitmap()
                    blurImage = classifier.isInappropriateImage(
                        bitmap = bitmap,
                        sfwThreshold = sfwThreshold,
                        nsfwThreshold = nsfwThreshold
                    )
                    isLoading = false
                }
                if (onSuccess != null) onSuccess(success)
            },
            contentDescription = contentDescription,
            error = error,
            onLoading = { loading ->
                isLoading = true
                if (onLoading != null) onLoading(loading)
            },
            onError = { error ->
                blurImage = false
                isLoading = false
                if (onError != null) onError(error)
            },
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
            clipToBounds = clipToBounds,
            modelEqualityDelegate = modelEqualityDelegate
        )

        if (placeholder != null && isLoading) {
            Image(
                painter = placeholder,
                contentDescription = contentDescription,
                modifier = Modifier
                    .matchParentSize()
                    .background(placeholderBackgroundColor),
                contentScale = contentScale,
                alignment = alignment,
                alpha = alpha,
            )
        }
    }
}