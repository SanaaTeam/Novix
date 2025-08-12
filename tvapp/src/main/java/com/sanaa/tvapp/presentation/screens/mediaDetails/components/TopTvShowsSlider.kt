package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.TvShowDetailsUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard

@Composable
fun TopTvShowsSlider(
    title: String,
    tvShows: List<TvShowDetailsUiModel>,
    onTvShowCardClicked: (Int) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.headLine.small,
            color = Theme.colors.title,
            modifier = Modifier.padding(horizontal = 36.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp)
        ) {
            items(tvShows.size) { index ->
                val movie = tvShows[index]
                TvMediaPosterCard(
                    title = movie.title,
                    imageUrl = movie.posterUrl?.orEmpty(),
                    onCardClick = {
                        onTvShowCardClicked(movie.id)
                    },
                )
            }
        }
    }
}