package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.home.ImageList
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard

@Composable
fun MoviesSlider(
    moviesPagingData: LazyPagingItems<MovieDetailsUiModel>,
    onMovieCardClicked:(Int)->Unit,
    modifier: Modifier = Modifier
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
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
        ) {
            items(moviesPagingData.itemCount) { index ->
                val movie = moviesPagingData[index]
                if (movie != null) {
                    TvMediaPosterCard(
                        title = movie.title,
                        imageUrl = movie.posterUrl?:"",
                        onCardClick = {
                            onMovieCardClicked(movie.id)
                        },
                    )
                }
            }
        }
    }

}