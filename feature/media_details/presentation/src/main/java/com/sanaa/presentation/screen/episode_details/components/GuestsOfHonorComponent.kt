package com.sanaa.presentation.screen.episode_details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sanaa.designsystem.design_system.component.cards.ActorCard
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.R
import com.sanaa.presentation.model.ActorUiModel

@Composable
fun GuestsOfHonorComponent(
    guests: List<ActorUiModel>,
    onActorClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.guest_of_honor),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )

        guests.forEach {
            ActorCard(
                playedCharacter = it.character,
                actorName = it.name, actorImage = rememberAsyncImagePainter(
                    model = it.imageUrl,
                ),
                modifier = Modifier
                    .fillMaxWidth(),
                onCardClick = { onActorClick(it.id) }
            )
        }
    }
}