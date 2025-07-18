package com.sanaa.presentation.screen.movie_categories

import androidx.compose.material3.BottomAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sanaa.presentation.R
import entity.Genre

data class MovieCategoriesScreenUiState(
    val title: Genre = Genre.DRAMA,
    val movies: List<MovieCardUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showBottomSheet: Boolean = false
    )

data class MovieCardUiModel(
    val id: Int = 0,
    val title: String = "",
    val imageUrl: String = "",
    val rating: Float = 0.0f,
)

@Composable
fun Genre.toLocalizedString(): String {
    return when (this) {
        Genre.DRAMA -> stringResource(R.string.genre_drama)
        Genre.COMEDY -> stringResource(R.string.genre_comedy)
        Genre.ADVENTURE -> stringResource(R.string.genre_adventure)
        Genre.ACTION -> stringResource(R.string.genre_action)
        Genre.ROMANCE -> stringResource(R.string.genre_romance)
        Genre.FANTASY -> stringResource(R.string.genre_fantasy)
        Genre.SCIENCE_FICTION -> stringResource(R.string.genre_science_fiction)
        Genre.HORROR -> stringResource(R.string.genre_horror)
        Genre.CRIME -> stringResource(R.string.genre_crime)
        Genre.ANIMATION -> stringResource(R.string.genre_animation)
        Genre.DOCUMENTARY -> stringResource(R.string.genre_documentary)
        Genre.THRILLER -> stringResource(R.string.genre_thriller)
        Genre.MUSIC -> stringResource(R.string.genre_music)
        Genre.MYSTERY -> stringResource(R.string.genre_mystery)
        Genre.KIDS -> stringResource(R.string.genre_kids)
        Genre.HISTORY -> stringResource(R.string.genre_history)
        Genre.FAMILY -> stringResource(R.string.genre_family)
        Genre.WAR -> stringResource(R.string.genre_war)
        Genre.TALK -> stringResource(R.string.genre_talk)
        Genre.SOAP -> stringResource(R.string.genre_soap)
        Genre.REALITY -> stringResource(R.string.genre_reality)
        Genre.NEWS -> stringResource(R.string.genre_news)
        Genre.TV_MOVIE -> stringResource(R.string.genre_tv_movie)
        Genre.WESTERN -> stringResource(R.string.genre_western)
        Genre.WAR_AND_POLITICS -> stringResource(R.string.genre_war_and_politics)
        Genre.SCI_FI_AND_FANTASY -> stringResource(R.string.genre_sci_fi_and_fantasy)
        Genre.ACTION_AND_ADVENTURE -> stringResource(R.string.genre_action_and_adventure)
    }
}