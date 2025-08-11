package com.sanaa.tvapp.presentation.screens.mediaDetails.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.component.text.AppText
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.presentation.screens.mediaDetails.model.ActorUiModel
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.RemoteImagePlaceholder
import com.sanaa.tvapp.presentation.screens.searchScreen.componants.TvMediaPosterCard
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R

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
                TvMediaPosterCard(
                    title = actor.name,
                    imageUrl = actor.imageUrl ?: "",
                    onCardClick = {
                        onActorCardClicked(actor.id)
                    },
                )
            }
        }
    }
}

