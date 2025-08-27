package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.home.component.Title
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard

@Composable
fun TopMoviesSlider(
    onMovieCardClicked: (Int) -> Unit,
    movies: List<MovieDetailsUiModel>,
    modifier: Modifier = Modifier,
    title: String=stringResource(R.string.top_movies_pick),
) {
    Column(modifier = modifier) {
        Title(
            title = title,
            modifier = Modifier.padding(horizontal = 36.dp, vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
        ) {
            items(movies.size) { index ->
                val movie = movies[index]

                FocusableMediaCard(
                    imageUrl = movie.posterUrl ?: "",
                    titleText = movie.title,
                    onClick = { onMovieCardClicked(movie.id) }
                )
            }
        }
    }
}