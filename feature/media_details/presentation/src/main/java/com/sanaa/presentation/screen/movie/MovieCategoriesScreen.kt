package com.sanaa.presentation.screen.movie

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.component.top_bar.TopBarClickableIcon
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.screen.state.MovieCardUiModel
import com.sanaa.presentation.screen.state.MovieCategoriesUiModel

@Composable
fun MovieCategoriesScreen(
    state: MovieCategoriesUiModel,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSaveIconClick: () -> Unit = {}
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }
    Column(modifier = Modifier) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTopBar(
                leftContent = {
                    TopBarClickableIcon(
                        icon = painterResource(id = R.drawable.icon_arrow_back),
                        onClick = onBackClick
                    )
                },
                screenTitle = state.title,
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.movies) { movieCard ->
                    MovieSeriesPosterCard(
                        boastImage = {
                            RemoteCensoredImageViewer(
                                imageUrl = movieCard.imageUrl,
                                modifier = Modifier.fillMaxWidth(),
                                blurRadius = 150,
                                sfwThreshold = 0.75f,
                                nsfwThreshold = 0.15f,
                                contentDescription = movieCard.title,
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(placeholderResId),
                                error = painterResource(placeholderResId),
                                placeholderBackgroundColor = Theme.colors.surface,
                                hintText = stringResource(com.sanaa.presentation.R.string.unsuitable_image),
                                textStyle = Theme.textStyle.body.small,
                                iconSize = 24.dp,
                            )

                        }, topLeftContent = {
                            SaveIconChip(onClick = onSaveIconClick)
                        }
                    )

                }
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCategoriesScreenPreview() {
    val sampleMovieCards = listOf(
        MovieCardUiModel(
            id = 1,
            title = "Inception",
            imageUrl = "https://dummyimage.com/300x450/000/fff&text=Inception",
            rating = "8.8"
        ),
        MovieCardUiModel(
            id = 2,
            title = "Interstellar",
            imageUrl = "https://dummyimage.com/300x450/000/fff&text=Interstellar",
            rating = "8.6"
        ),
        MovieCardUiModel(
            id = 3,
            title = "Tenet",
            imageUrl = "https://dummyimage.com/300x450/000/fff&text=Tenet",
            rating = "7.4"
        ),
        MovieCardUiModel(
            id = 4,
            title = "Dune",
            imageUrl = "https://dummyimage.com/300x450/000/fff&text=Dune",
            rating = "8.2"
        )
    )

    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        MovieCategoriesScreen(
            MovieCategoriesUiModel(
                title = "Crime",
                movies = sampleMovieCards
            ),
            onBackClick = {  },
            onSaveIconClick = {  }
        )
    }
}