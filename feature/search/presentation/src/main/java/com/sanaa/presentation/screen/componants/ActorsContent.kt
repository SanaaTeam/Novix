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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.paging.LoadState
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator

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

        if (actors.loadState.append is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    WavyProgressIndicator()
                }
            }
        }
    }
}
