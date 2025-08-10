package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.tvapp.presentation.screens.searchScreen.MovieUiModel

@Composable
fun MovieTvContent(
    movies: LazyPagingItems<MovieUiModel>,
    onClick: (MovieUiModel) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
    ) {
        items(movies.itemCount) { index ->
            movies[index]?.let { movie ->
                FocusableMediaCard(
                    imageUrl = movie.imageUrl,
                    titleText = movie.title,
                    onClick = { onClick(movie) }
                )
            }
        }
    }
}