package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.component.RemoteImagePlaceholder
import com.sanaa.presentation.component.cards.MediaPosterCard
import com.sanaa.presentation.component.cards.SaveIconChip
import com.sanaa.presentation.model.MovieUiModel

@Composable
fun MoreLikeThisCard(
    movie: MovieUiModel,
    onBookmarkClick: () -> Unit,
    onMovieClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    MediaPosterCard(
        modifier = modifier,
        onCardClick = onMovieClick,
        boastImage = {
            RemoteBlurredHaramImageViewer(
                imageUrl = movie.posterUrl.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                blurRadius = 150,
                haramThreshold = 0.2f,
                nonHaramThreshold = 0.7f,
                contentDescription = movie.title,
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
                    icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                )
            }
        },
        topLeftContent = {
            SaveIconChip(
                isSaved = movie.isBookmarked,
                onClick = onBookmarkClick
            )
        }
    )
}

