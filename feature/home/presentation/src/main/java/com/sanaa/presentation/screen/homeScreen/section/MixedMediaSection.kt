package com.sanaa.presentation.screen.homeScreen.section

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.section_header.InlineAction
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.RemoteImagePlaceholder
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.model.MediaItem
import com.sanaa.presentation.model.MediaType
import kotlin.math.absoluteValue

val demoMediaList = listOf(
    MediaItem(
        id = 1,
        title = "media 1",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.MOVIE
    ),
    MediaItem(
        id = 2,
        title = "media 2",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    ),
    MediaItem(
        id = 3,
        title = "media 3",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.MOVIE
    ),
    MediaItem(
        id = 4,
        title = "media 4",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    )
)

@Composable
fun MixedMediaSection(
    modifier: Modifier = Modifier,
    headerLabel: String,
    mediaItems: List<MediaItem>
) {

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { mediaItems.size })
    val focusedWidth = 210.dp
    val unFocusedWidth = 74.dp



    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(
            title = headerLabel,
            rightContent = {
                InlineAction(
                    onClick = {},
                )
            }
        )
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(start = 0.dp, end = 160.dp),
            pageSpacing = -170.dp,
            modifier = Modifier.fillMaxWidth()
        )
        { page ->

            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                    .coerceIn(0f, 1f)
            val animatedWidth by animateDpAsState(
                targetValue = lerp(start = unFocusedWidth, stop = focusedWidth, 1f - pageOffset),
                label = "animatedWidth"
            )

            Box(
                modifier = Modifier
                    .width(animatedWidth)
                    .height(210.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                MediaPosterCard(
                    ratio = animatedWidth / 210.dp,
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {}
                        )
                    },
                    onCardClick = {

                    },
                    posterImage = {
                        RemoteBlurredHaramImageViewer(
                            imageUrl = mediaItems[page].imageUrl,
                            modifier = Modifier,
                            blurRadius = 150,
                            haramThreshold = 0.2f,
                            nonHaramThreshold = 0.7f,
                            placeholderContent = {
                            RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            contentDescription = mediaItems[page].title,
                        ) {
                            OnBlurContent(
                                hintText = stringResource(R.string.unsuitable_image),
                                textStyle = Theme.textStyle.body.small.copy(
                                    color = Color(0x99FFFFFF)
                                ),
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


@PreviewLightDark
@Composable
fun MixedMediaSectionPreview(modifier: Modifier = Modifier) {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier.fillMaxSize()
        ) {
            MixedMediaSection(
                mediaItems = demoMediaList,
                headerLabel = "Top Rating"
            )
        }

    }
}