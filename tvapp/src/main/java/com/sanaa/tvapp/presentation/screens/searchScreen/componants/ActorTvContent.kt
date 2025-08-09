package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.tvapp.presentation.screens.searchScreen.ActorUiModel


@Composable
fun ActorTvContent(
    actors: LazyPagingItems<ActorUiModel>,
    onClick: (ActorUiModel) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 36.dp, vertical = 24.dp)
    ) {
        items(actors.itemCount) { index ->
            actors[index]?.let { actor ->
                FocusableMediaCard(
                    imageUrl = actor.imageUrl,
                    titleText = actor.name,
                    onClick = { onClick(actor) }
                )
            }
        }
    }
}
