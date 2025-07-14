package com.sanaa.image_viewer.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.DefaultModelEqualityDelegate
import coil.compose.EqualityDelegate
import coil.request.ImageRequest
import com.sanaa.image_viewer.classifier.TfLiteImageClassifier
import com.sanaa.inappropriate_image_viewer_library.R
import com.skydoves.cloudy.cloudy
import kotlinx.coroutines.launch

/**
 * A composable build on coil [AsyncImage] that executes an [ImageRequest] asynchronously and
 * analyse it renders the result then.
 *
 * @param imageUrl path for remote image url.
 * @param contentDescription Text used by accessibility services to describe what this image
 *  represents. This should always be provided unless this image is used for decorative purposes,
 *  and does not represent a meaningful action that a user can take.
 * @param modifier Modifier used to adjust the layout algorithm or draw decoration content.
 * @param placeholder A [Painter] that is displayed while the image is loading.
 * @param placeholderBackgroundColor A [Color] that is displayed if the placeholder has transparent
 * parts.
 * @param error A [Painter] that is displayed when the image request is unsuccessful.
 * @param onLoading Called when the image request begins loading.
 * @param onSuccess Called when the image request completes successfully.
 * @param onError Called when the image request completes unsuccessfully.
 * @param alignment Optional alignment parameter used to place the [AsyncImagePainter] in the given
 *  bounds defined by the width and height.
 * @param contentScale Optional scale parameter used to determine the aspect ratio scaling to be
 *  used if the bounds are a different size from the intrinsic size of the [AsyncImagePainter].
 * @param alpha Optional opacity to be applied to the [AsyncImagePainter] when it is rendered
 *  onscreen.
 * @param colorFilter Optional [ColorFilter] to apply for the [AsyncImagePainter] when it is
 *  rendered onscreen.
 * @param filterQuality Sampling algorithm applied to a bitmap when it is scaled and drawn into the
 *  destination.
 * @param clipToBounds If true, clips the content to its bounds. Else, it will not be clipped.
 * @param modelEqualityDelegate Determines the equality of image. This controls whether this
 *  composable is redrawn and a new image request is launched when the outer composable recomposes.
 * @param blurRadius Radius of the blur along both the x and y axis.
 * @param sfwThreshold If the sfw score for this image is less than the threshold, the blur would be
 * enabled. Range is [0.0f, 1.0f].
 * @param nsfwThreshold If the nsfw score for this image is more than the threshold, the blur would
 * be enabled. Range is [0.0f, 1.0f].
 * @param hintText text to be displayed on the blur.
 * @param textStyle style of text, color is not applicable.
 * @param revealBlurIcon icon that disable the blur when clicked.
 */
@Composable
fun RemoteCensoredImageViewer(
    imageUrl: String,
    contentDescription: String?,
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
    isBlurEnabled: Boolean = true,
    @FloatRange(from = 0.0, to = 1.0) sfwThreshold: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) nsfwThreshold: Float = 0.5f,
    hintText: String = "Unsuitable image",
    textStyle: TextStyle = TextStyle.Default,
    revealBlurIcon: Painter = painterResource(R.drawable.icon_eye_slash),
    iconSize: Dp = 24.dp,
) {
    val context = LocalContext.current
    val classifier = remember { TfLiteImageClassifier(context) }

    var blurImage by rememberSaveable { mutableStateOf(isBlurEnabled) }
    var isLoading by rememberSaveable { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build(),
            modifier = Modifier.cloudy(radius = blurRadius, enabled = (blurImage && isBlurEnabled)),
            onSuccess = { success ->
                coroutineScope.launch {
                    if (isBlurEnabled) {
                        val bitmap = success.result.drawable.toBitmap()
                        blurImage = classifier.isInappropriateImage(
                            bitmap = bitmap,
                            sfwThreshold = sfwThreshold,
                            nsfwThreshold = nsfwThreshold
                        )
                    }
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

        if (blurImage && !isLoading && isBlurEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x52000000)),

                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Image(
                    painter = revealBlurIcon,
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .size(iconSize)
                        .clickable { blurImage = false },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color(0x99FFFFFF)),
                )

                Text(
                    text = hintText,
                    style = textStyle.copy(color = Color(0x99FFFFFF)),
                )
            }
        }
    }
}