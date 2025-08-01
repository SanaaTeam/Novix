package com.sanaa.presentation.screen.episodeDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.CustomText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.presentation.model.ActorUiModel
import com.sanaa.presentation.shared_component.cards.ActorCard

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
        CustomText(
            text = stringResource(R.string.guest_of_honor),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title
        )

        guests.forEach {
            ActorCard(
                playedCharacter = it.character,
                actorName = it.name,
                imageUrl = it.imageUrl.orEmpty(),
                modifier = Modifier
                    .fillMaxWidth(),
                onCardClick = { onActorClick(it.id) }
            )
        }
    }
}