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
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard

@Composable
fun SimilarMoviesSlider(
    moviesPagingData: LazyPagingItems<MovieDetailsUiModel>,
    onMovieCardClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        AppText(
            text = stringResource(R.string.more_like_this),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
            modifier = Modifier.padding(horizontal = 36.dp, vertical = 8.dp)
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
        ) {
            items(moviesPagingData.itemCount) { index ->
                val movie = moviesPagingData[index]
                if (movie != null) {
                    FocusableMediaCard(
                        imageUrl = movie.posterUrl ?: "",
                        titleText = movie.title,
                        onClick = { onMovieCardClicked(movie.id) }
                    )
                }
            }
        }
    }
}