package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.MovieDetailsUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard

@Composable
fun MoviesPaginatedSlider(
    moviesPagingData: LazyPagingItems<MovieDetailsUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        AppText(
            text = stringResource(com.sanaa.tvapp.R.string.more_like_this),
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
                        posterImage = {
                            RemoteBlurredSensitiveImage(
                                imageUrl = movie.posterUrl?:"",
                                modifier = Modifier.fillMaxWidth(),
                                sensitiveContentThreshold = 0.2f,
                                safeContentThreshold = 0.7f,
                                placeholderContent = {
                                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                                },
                                errorContent = {
                                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                                },
                                contentDescription = movie.title,
                            ) {
                                OnBlurContent(
                                    hintText = stringResource(com.sanaa.tvapp.R.string.unsuitable_image),
                                    textStyle = Theme.textStyle.body.small.copy(
                                        color = Color(0x99FFFFFF)
                                    ),
                                    iconSize = 24.dp,
                                    icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                                )
                            }
                        }
                    )
                }
            }
        }
    }

}