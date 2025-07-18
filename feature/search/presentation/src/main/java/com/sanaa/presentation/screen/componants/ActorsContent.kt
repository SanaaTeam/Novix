package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.screen.state.ActorUiModel

@Composable
fun ActorsContent(
    actors: List<ActorUiModel>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
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

@PreviewLightDark
@Composable
private fun PreviewActorsContent() {
    val list = listOf(
        ActorUiModel(1, "Actor1", "https://image.com"),
        ActorUiModel(2, "Actor2", "https://image.com"),
        ActorUiModel(3, "Actor3", "https://image.com"),
    )

    NovixTheme(isSystemInDarkTheme()) {
        ActorsContent(list)
    }
}
