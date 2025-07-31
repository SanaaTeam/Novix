package com.sanaa.image_viewer.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.request.ImageRequest
import com.sanaa.image_viewer.classifier.TfLiteImageClassifier
import com.sanaa.image_viewer.modifier.blurry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A composable executes an [ImageRequest] asynchronously and
 * analyse the image to blur haram content.
 *
 * @param imageUrl path for remote image url.
 * @param contentDescription Text used by accessibility services to describe what this image
 *  represents. This should always be provided unless this image is used for decorative purposes,
 *  and does not represent a meaningful action that a user can take.
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content.
 * @param placeholderContent A [Composable] that is displayed while the image is loading.
 * @param errorContent A [Composable] that is displayed when the image request is unsuccessful.
 * @param onSuccess Called when the image request completes successfully.
 * @param onError Called when the image request completes unsuccessfully.
 * @param blurRadius Radius of the blur along both the x and y axis.
 * @param nonHaramThreshold If the 'is non-haram' score for this image is less than the threshold,
 *  the blur would be enabled. Range is [0.0f, 1.0f].
 * @param haramThreshold If the 'is haram' score for this image is more than the threshold, the blur
 *  would be enabled. Range is [0.0f, 1.0f].
 * @param onBlurContent composable that would be displayed when the image is blurred.
 */
@Composable
fun RemoteBlurredHaramImageViewer(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 10.dp,
    isBlurEnabled: Boolean = true,
    placeholderContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {},
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    @FloatRange(from = 0.0, to = 1.0) haramThreshold: Float = 0.2f,
    @FloatRange(from = 0.0, to = 1.0) nonHaramThreshold: Float = 0.7f,
    onBlurContent: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    val classifier = remember { TfLiteImageClassifier(context) }

    var bitmapToDisplay by remember { mutableStateOf<Bitmap?>(null) }
    var isHaram by rememberSaveable { mutableStateOf(false) }
    var requestState by rememberSaveable { mutableStateOf(RequestState.LOADING) }

    LaunchedEffect(imageUrl) {
        requestState = RequestState.LOADING

        val result = runCatching {
            withContext(Dispatchers.IO) {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()
                val response = loader.execute(request)
                val bitmap = (response.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    val classificationResult = classifier.isInappropriateImage(
                        bitmap,
                        nonHaramThreshold,
                        haramThreshold
                    )
                    Pair(bitmap, classificationResult)
                } else null
            }
        }

        result.onSuccess { pair ->
            if (pair != null) {
                bitmapToDisplay = pair.first
                isHaram = pair.second
                requestState = RequestState.SUCCESS
                onSuccess?.invoke()
            } else {
                requestState = RequestState.ERROR
                onError?.invoke()
            }
        }.onFailure {
            requestState = RequestState.ERROR
            onError?.invoke()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (requestState) {
            RequestState.LOADING -> placeholderContent()
            RequestState.SUCCESS -> {
                bitmapToDisplay?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = contentDescription,
                        modifier = Modifier
                            .fillMaxSize()
                            .blurry(
                                isBlurForced = false,
                                isImageSafe = !isHaram,
                                blurRadius = blurRadius,
                                isBlurEnabled = isBlurEnabled,
                                bitmap = bmp
                            ),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            RequestState.ERROR -> errorContent()
        }

        if (requestState == RequestState.SUCCESS && isHaram && isBlurEnabled) {
            onBlurContent()
        }
    }
}

enum class RequestState {
    LOADING,
    SUCCESS,
    ERROR
}
