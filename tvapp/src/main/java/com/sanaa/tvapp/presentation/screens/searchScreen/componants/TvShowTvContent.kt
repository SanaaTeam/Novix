package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.tv.foundation.lazy.list.TvLazyRow
import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel


@Composable
fun TvShowTvContent(
    shows: LazyPagingItems<TvShowUiModel>,
    onClick: (TvShowUiModel) -> Unit
) {
    TvLazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
    ) {
        items(shows.itemCount) { index ->
            shows[index]?.let { show ->
                FocusableMediaCard(
                    imageUrl = show.imageUrl,
                    titleText = show.title,
                    onClick = { onClick(show) }
                )
            }
        }
    }
}
