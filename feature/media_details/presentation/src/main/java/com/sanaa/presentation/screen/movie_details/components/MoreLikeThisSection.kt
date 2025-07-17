package com.sanaa.presentation.screen.movie_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.model.SimilarMovieUiModel
import com.sanaa.presentation.R as presentationR

@Composable
fun MoreLikeThisSection(
    similarMovies: List<SimilarMovieUiModel>,
    onBookmarkClick: (SimilarMovieUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(top = 16.dp)
    ) {
        Text(
            text = stringResource(id = presentationR.string.more_like_this),
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 7000.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(similarMovies.size) { index ->
                val movie = similarMovies[index]
                MoreLikeThisCard(
                    movie = movie,
                    onBookmarkClick = { onBookmarkClick(movie) }
                )
            }
        }
    }
}
