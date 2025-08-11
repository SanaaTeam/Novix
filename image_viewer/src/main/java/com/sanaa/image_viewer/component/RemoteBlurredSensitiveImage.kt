package com.sanaa.image_viewer.component

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sanaa.image_viewer.classifier.TfLiteImageClassifier
import com.sanaa.image_viewer.modifier.blurry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A composable executes an [ImageRequest] asynchronously and
 * analyse the image to blur sensitive content.
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
 * @param safeContentThreshold If the 'is non-sensitive' score for this image is less than the threshold,
 *  the blur would be enabled. Range is [0.0f, 1.0f].
 * @param sensitiveContentThreshold If the 'is sensitive' score for this image is more than the threshold, the blur
 *  would be enabled. Range is [0.0f, 1.0f].
 * @param onBlurContent composable that would be displayed when the image is blurred.
 */
@Composable
fun RemoteBlurredSensitiveImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    blurRadius: Dp = 30.dp,
    isBlurEnabled: Boolean = true,
    placeholderContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {},
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    @FloatRange(from = 0.0, to = 1.0) sensitiveContentThreshold: Float = 0.2f,
    @FloatRange(from = 0.0, to = 1.0) safeContentThreshold: Float = 0.7f,
    onBlurContent: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    val classifier = remember(context) { TfLiteImageClassifier(context) }

    var bitmapToBlur by remember(imageUrl) { mutableStateOf<Bitmap?>(null) }
    var isSensitive by rememberSaveable { mutableStateOf(false) }
    var requestState by rememberSaveable { mutableStateOf(RequestState.LOADING) }

    val coroutineScope = rememberCoroutineScope()

    val request = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false) // Required to get Bitmap from Drawable
            .build()
    }

    LaunchedEffect(imageUrl) {
        if (requestState != RequestState.LOADING) return@LaunchedEffect
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val loader = ImageLoader(context)

                val response = loader.execute(request)
                val bitmap = (response.drawable as? BitmapDrawable)?.bitmap
                if (bitmap != null) {
                    isSensitive = classifier.isInappropriateImage(
                        bitmap,
                        safeContentThreshold,
                        sensitiveContentThreshold
                    )

//                    store bitmap to blur for older android version
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S && isSensitive) {
                        bitmapToBlur = bitmap
                    }

                    requestState = RequestState.SUCCESS
                    onSuccess?.invoke()
                } else {
                    throw Exception("failed to fetch image bitmap")
                }

            } catch (_: Exception) {
                requestState = RequestState.ERROR
                onError?.invoke()
            }
        }
    }

    LaunchedEffect(bitmapToBlur) {
        //restore bitmap to blur only for older android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S || !isSensitive) return@LaunchedEffect
        if (bitmapToBlur != null || requestState != RequestState.SUCCESS) return@LaunchedEffect

        coroutineScope.launch(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val response = loader.execute(request)
            bitmapToBlur = (response.drawable as? BitmapDrawable)?.bitmap
        }
    }


    Box(modifier = modifier.fillMaxSize()) {
        when (requestState) {
            RequestState.LOADING -> placeholderContent()
            RequestState.SUCCESS -> {
                AsyncImage(
                    model = request,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize()
                        .blurry(
                            isBlurForced = false,
                            isImageSafe = !isSensitive,
                            blurRadius = blurRadius,
                            isBlurEnabled = isBlurEnabled,
                            bitmap = bitmapToBlur
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            RequestState.ERROR -> errorContent()
        }

        if (requestState == RequestState.SUCCESS && isSensitive && isBlurEnabled) {
            onBlurContent()
        }
    }
}

enum class RequestState {
    LOADING,
    SUCCESS,
    ERROR
}
