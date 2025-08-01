package com.sanaa.presentation.screen.series.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
fun CastComponent(
    casts: List<ActorUiModel>, onActorClicked: (Int) -> Unit, modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        CustomText(
            text = stringResource(R.string.cast),
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                casts,
                key = { item -> item.id }
            ) { cast ->
                ActorCard(
                    actorName = cast.name,
                    playedCharacter = cast.character,
                    imageUrl = cast.imageUrl.orEmpty(),
                    modifier = Modifier.width(296.dp),
                    onCardClick = {
                        onActorClicked(cast.id)
                    })
            }
        }
    }
}