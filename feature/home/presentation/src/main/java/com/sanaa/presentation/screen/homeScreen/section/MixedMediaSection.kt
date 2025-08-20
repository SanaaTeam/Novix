package com.sanaa.presentation.screen.homeScreen.section

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.component.section_header.InlineAction
import com.sanaa.designsystem.design_system.component.section_header.SectionHeader
import com.sanaa.designsystem.design_system.component.slider.CarouselSlider
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.home.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.components.RemoteImagePlaceholder
import com.sanaa.presentation.providers.LocalSafeContentThreshold
import com.sanaa.presentation.state.MediaItemUiState
import com.sanaa.presentation.state.MediaTypeUiState

@Composable
fun MixedMediaSection(
    modifier: Modifier = Modifier,
    headerLabel: String,
    mediaItems: List<MediaItemUiState>,
    onMediaClick: (MediaItemUiState) -> Unit,
    onSaveIconClicked: (MediaItemUiState) -> Unit,
    onViewAllClick: () -> Unit = {}
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionHeader(
            title = headerLabel,
            rightContent = {
                InlineAction(
                    onClick = onViewAllClick
                )
            }
        )
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
        ) {
            CarouselSlider(
                items = mediaItems,
                preferredItemWidth = 158.dp,
                itemSpacing = 8.dp,
                contentPadding = PaddingValues(0.dp),
                maxSmallItemWidth = 74.dp,
                minSmallItemWidth = 74.dp,
            ) { item, isFocused ->
                MediaPosterCard(
                    height = 210.dp,
                    topLeftContent = {
                        Column {
                            AnimatedVisibility(
                                visible = isFocused,
                                enter = slideInHorizontally() + fadeIn(),
                                exit = slideOutHorizontally() + fadeOut()
                            ) {
                                if(item.mediaTypeUiState == MediaTypeUiState.MOVIE) {
                                    SaveIconChip(
                                        onClick = { onSaveIconClicked(item) },
                                        isSaved = item.isSaved
                                    )
                                }
                            }
                        }
                    },
                    onCardClick = {
                        onMediaClick(item)
                    },
                    posterImage = {
                        RemoteBlurredSensitiveImage(
                            imageUrl = item.imageUrl.orEmpty(),
                            modifier = Modifier,
                            sensitiveContentThreshold = 0.2f,
                            safeContentThreshold = LocalSafeContentThreshold.current,
                            isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                            placeholderContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            errorContent = {
                                RemoteImagePlaceholder(Modifier.fillMaxSize())
                            },
                            contentDescription = item.title,
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

val demoMediaList = listOf(
    MediaItemUiState(
        id = 1,
        title = "media 1",
        imageUrl = "",
        rating = " 9.9",
        mediaTypeUiState = MediaTypeUiState.MOVIE
    ),
    MediaItemUiState(
        id = 2,
        title = "media 2",
        imageUrl = "",
        rating = " 9.9",
        mediaTypeUiState = MediaTypeUiState.TV_SHOW
    ),
    MediaItemUiState(
        id = 3,
        title = "media 3",
        imageUrl = "",
        rating = " 9.9",
        mediaTypeUiState = MediaTypeUiState.MOVIE
    ),
    MediaItemUiState(
        id = 4,
        title = "media 4",
        imageUrl = "",
        rating = " 9.9",
        mediaTypeUiState = MediaTypeUiState.TV_SHOW
    ),
)