package com.sanaa.presentation.components.lists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.sanaa.presentation.components.cards.PersonCard
import com.sanaa.presentation.state.PersonUiState

@Composable
fun PersonList(
    persons: LazyPagingItems<PersonUiState>,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(persons.itemCount) { index ->
            persons[index]?.let { person ->
                PersonCard(
                    actorName = person.name,
                    imageUrl = person.imageUrl,
                    playedCharacter = person.character,
                    onCardClick = { onItemClick(person.id) }
                )
            }
        }
    }
}
