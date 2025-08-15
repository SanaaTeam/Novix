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
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.movieDetails.MoreLikeThisListener
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip

@Composable
fun MoreLikeThisCard(
    movie: MovieUiModel,
    interactionListener: MoreLikeThisListener,
    modifier: Modifier = Modifier
) {

    MediaPosterCard(
        modifier = modifier,
        onCardClick = { interactionListener.onMovieClick(movie.id) },
        posterImage = {
            RemoteBlurredSensitiveImage(
                imageUrl = movie.posterUrl.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                sensitiveContentThreshold = 0.2f,
                isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                safeContentThreshold = LocalSafeContentThreshold.current,
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
                isSaved = movie.isSaved,
                onClick = { interactionListener.onBookmarkClick(movie) }
            )
        }
    )
}

