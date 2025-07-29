package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.presentation.screen.componants.cards.ActorCard
import com.sanaa.presentation.screen.state.ActorUiModel

@Composable
fun ActorsContent(
    actors: LazyPagingItems<ActorUiModel>,
    onActorClick: (ActorUiModel) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(actors.itemCount) { index ->
            actors[index]?.let { actor ->
                ActorCard(
                    actorName = actor.name,
                    imageUrl = actor.imageUrl,
                    playedCharacter = null,
                    onCardClick = { onActorClick(actor) }
                )
            }
        }
    }
}
