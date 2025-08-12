package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.tvapp.presentation.screens.searchScreen.TvShowUiModel


@Composable
fun TvShowTvContent(
    shows: LazyPagingItems<TvShowUiModel>,
    onClick: (Int) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
    ) {
        items(shows.itemCount) { index ->
            shows[index]?.let { show ->
                FocusableMediaCard(
                    imageUrl = show.imageUrl,
                    titleText = show.title,
                    onClick = { onClick(show.id) }
                )
            }
        }
    }
}
