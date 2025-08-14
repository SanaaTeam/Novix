package com.sanaa.presentation.screen.movieDetails.components

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.model.MovieUiModel
import com.sanaa.presentation.screen.movieDetails.MovieDetailsTopBarListener
import com.sanaa.designsystem.R as designR

@Composable
fun MovieDetailsTopBar(
    interactionListener: MovieDetailsTopBarListener,
    movie: MovieUiModel,
    modifier: Modifier = Modifier,
) {
    TopBar(
        leftContent = {
            TopBarClickableIcon(
                icon = painterResource(designR.drawable.icon_back),
                onClick = { interactionListener.onBackClick() }
            )
        },
        rightContent = {
            TopBarClickableIcon(
                icon = if (movie.isSaved)
                    painterResource(designR.drawable.icon_saved)
                else
                    painterResource(R.drawable.icon_save),
                onClick = { interactionListener.onBookmarkClick(movie) }
            )
        },
        modifier = modifier
            .systemBarsPadding()
            .zIndex(10f)
    )
}