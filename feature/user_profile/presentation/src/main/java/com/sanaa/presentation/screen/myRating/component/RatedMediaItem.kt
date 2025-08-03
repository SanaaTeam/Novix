package com.sanaa.presentation.screen.myRating.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.chips.DeleteIconChip
import com.sanaa.designsystem.design_system.component.chips.MediaRatingChip
import com.sanaa.designsystem.design_system.component.poster.MediaPosterCard
import com.sanaa.designsystem.design_system.component.poster.RemoteImagePlaceholder
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.model.RatedMediaUiModel

@Composable
fun RatedMediaItem(
    media: RatedMediaUiModel,
    onDeleteClick: (mediaId: Int, mediaType: String) -> Unit,
    onCardClick: (mediaId: Int, mediaType: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        MediaPosterCard(
            onCardClick = { onCardClick(media.id, media.mediaType) },
            posterImage = {
                RemoteBlurredHaramImageViewer(
                    imageUrl = media.posterImageUrl.orEmpty(),
                    modifier = Modifier.fillMaxSize(),
                    haramThreshold = 0.2f,
                    nonHaramThreshold = 0.7f,
                    contentDescription = media.title,
                    placeholderContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
                    errorContent = {
                        RemoteImagePlaceholder(Modifier.fillMaxSize())
                    },
                ) {
                    OnBlurContent(
                        hintText = stringResource(R.string.unsuitable_image),
                        textStyle = Theme.textStyle.body.small.copy(
                            color = Color(0x99FFFFFF)
                        ),
                        iconSize = 24.dp,
                        icon = painterResource(R.drawable.icon_eye_slash),
                    )
                }
            },
            topLeftContent = {
                media.rating?.let {
                    MediaRatingChip(
                        rating = it.toString()
                    )
                }
            },
        )
        DeleteIconChip(
            onClick = { onDeleteClick(media.id, media.mediaType) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}