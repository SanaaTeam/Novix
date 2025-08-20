package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.R
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.FocusableMediaCard


@Composable
fun CastSlider(
    cast: List<ActorUiModel>,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.cast),
    onActorCardClicked: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        AppText(
            text = title,
            style = Theme.textStyle.title.medium,
            color = Theme.colors.title,
            modifier = Modifier.padding(horizontal = 36.dp, vertical = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
        ) {
            items(cast) { actor ->
                FocusableMediaCard(
                    imageUrl = actor.imageUrl ?: "",
                    titleText = actor.name,
                    onClick = { onActorCardClicked(actor.id) }
                )
            }
        }
    }
}