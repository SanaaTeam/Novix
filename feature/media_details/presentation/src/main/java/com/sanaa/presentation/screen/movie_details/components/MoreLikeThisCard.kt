package com.sanaa.presentation.screen.movie_details.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.module.MovieUiModel
import com.sanaa.designsystem.R as designR
import com.sanaa.presentation.R as presentationR

@Composable
fun MoreLikeThisCard(
    movie: MovieUiModel,
    onBookmarkClick: () -> Unit,
    onMovieClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        designR.drawable.movie_placeholder_dark
    } else {
        designR.drawable.movie_placeholder_light
    }

    MovieSeriesPosterCard(
        modifier = modifier,
        onCardClick = onMovieClick,
        boastImage = {
            RemoteBlurredHaramImageViewer(
                imageUrl = movie.posterUrl.orEmpty(),
                modifier = Modifier.fillMaxSize(),
                blurRadius = 150,
                haramThreshold = 0.2f,
                nonHaramThreshold = 0.7f,
                placeholder = painterResource(placeholderResId),
                error = painterResource(placeholderResId),
                contentDescription = movie.title
            ) {
                OnBlurContent(
                    hintText = stringResource(presentationR.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small.copy(
                        color = Color(0x99FFFFFF)
                    ),
                    iconSize = 24.dp,
                    icon = painterResource(designR.drawable.icon_eye_slash)
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

