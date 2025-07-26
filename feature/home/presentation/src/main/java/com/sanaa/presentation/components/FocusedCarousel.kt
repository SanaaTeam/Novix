package com.sanaa.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.state.MediaItem
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun FocusedCarousel(
    mediaItems: List<MediaItem>,
    modifier: Modifier = Modifier,
    focusedWidth: Dp = 158.dp,
    unfocusedWidth: Dp = 74.dp,
    cardHeight: Dp = 210.dp
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            // Snap to closest item after scroll ends
            val closest = listState.firstVisibleItemIndex +
                    if (listState.firstVisibleItemScrollOffset > with(density) { focusedWidth.toPx() } / 2) 1 else 0
            coroutineScope.launch {
                listState.animateScrollToItem(closest)
            }
        }
    }

    LazyRow(
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        itemsIndexed(mediaItems) { index, item ->
            // Compute how close this item is to the center
            val centerIndex = listState.firstVisibleItemIndex
            val offset = abs(centerIndex - index)
            val isFocused = offset == 0

            val animatedWidth by animateDpAsState(
                targetValue = if (isFocused) focusedWidth else unfocusedWidth,
                label = "cardWidth"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(cardHeight),
                contentAlignment = Alignment.Center
            ) {
                MediaPosterCard(
                    width = animatedWidth,
                    height = cardHeight,
                    topLeftContent = { SaveIconChip(onClick = {}) },
                    onCardClick = { /* Handle Click */ },
                    posterImage = {
                        RemoteBlurredHaramImageViewer(
                            imageUrl = item.imageUrl.orEmpty(),
                            modifier = Modifier,
                            blurRadius = 150,
                            haramThreshold = 0.2f,
                            nonHaramThreshold = 0.7f,
                            placeholderContent = {
//                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                                Image(
                                    painterResource(R.drawable.cenima_board),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            contentDescription = item.title,
                        ) {
                            OnBlurContent(
                                hintText = stringResource(R.string.unsuitable_image),
                                textStyle = Theme.textStyle.body.small.copy(color = Color(0x99FFFFFF)),
                                iconSize = 24.dp,
                                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                            )
                        }
                    }
                )
            }
        }
    }
}