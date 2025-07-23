package com.sanaa.presentation.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.presentation.components.cards.ActorCard
import com.sanaa.presentation.ui_state.ActorUiState
import androidx.compose.foundation.lazy.items


@Composable
fun ActorList(
    people: List<ActorUiState>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(people, key = { it.id }) { person ->
            ActorCard(
                actorName = person.name,
                actorImage = person.imagePainter,
                playedCharacter = person.character,
                onCardClick = { onItemClick(person.id) }
            )
        }
    }
}