package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.state.ActorUiModel

@Composable
fun ActorsContent(
    actors: List<ActorUiModel>
) {
    LazyColumn(
        modifier = Modifier
            .background(Theme.colors.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(actors) { actor ->
            ActorCard(
                actorName = actor.name,
                actorImage = rememberAsyncImagePainter(actor.imageUrl),
                playedCharacter = null
            )
        }
    }

}
