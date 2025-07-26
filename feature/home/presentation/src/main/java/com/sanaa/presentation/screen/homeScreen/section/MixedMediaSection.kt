package com.sanaa.presentation.screen.homeScreen.section

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.section_header.InlineAction
import com.sanaa.designsystem.design_system.component.section_header.NovixSectionHeader
import com.sanaa.designsystem.design_system.component.slider.CarouselSlider
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.components.RemoteImagePlaceholder
import com.sanaa.presentation.components.cards.MediaPosterCard
import com.sanaa.presentation.components.chips.SaveIconChip
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaType

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
    ),
    MediaItem(
        id = 5,
        title = "media 5",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    ),
    MediaItem(
        id = 6,
        title = "media 6",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    ),
    MediaItem(
        id = 7,
        title = "media 7",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    ),
    MediaItem(
        id = 8,
        title = "media 8",
        imageUrl = "",
        rating = 9.9f,
        mediaType = MediaType.TV_SHOW
    )
)

@Composable
fun MixedMediaSection(
    modifier: Modifier = Modifier,
    headerLabel: String,
    mediaItems: List<MediaItem>,
    onMediaClick: (MediaItem) -> Unit,
    onSaveIconClicked: (MediaItem) -> Unit,
    onViewAllClick: () -> Unit = {}
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixSectionHeader(
            title = headerLabel,
            rightContent = {
                InlineAction(
                    onClick = onViewAllClick
                )
            }
        )

//        FocusedCarousel(
//            mediaItems = mediaItems
//        )

        CarouselSlider(
            itemsCount = mediaItems.size,
            preferredItemWidth = 158.dp,
            itemSpacing = 8.dp,
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
            maxSmallItemWidth = 74.dp,
            minSmallItemWidth = 74.dp
        ) { page, isFocused ->
            val width = if (isFocused) 158.dp else 74.dp
            Box(
                modifier = Modifier
                    .height(210.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MediaPosterCard(
                    width = 158.dp,
                    height = 210.dp,
                    topLeftContent = {
                        SaveIconChip(
                            onClick = {
                                onSaveIconClicked(mediaItems[page])
                            }
                        )
                    },
                    onCardClick = {
                        onMediaClick(mediaItems[page])
                    },
                    posterImage = {
                        RemoteBlurredHaramImageViewer(
                            imageUrl = mediaItems[page].imageUrl.orEmpty(),
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
                headerLabel = "Top Rating",
                onMediaClick = { },
                onSaveIconClicked = { }
            )
        }

    }
}


