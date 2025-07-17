package com.sanaa.presentation.screens.actors.componants

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer

@Composable
fun PosterCard(imageUrl: String?) {
    MovieSeriesPosterCard(
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        boastImage = {
            RemoteCensoredImageViewer(
                imageUrl = imageUrl ?: "",
                modifier = Modifier,
                contentScale = ContentScale.Crop,
                blurRadius = 150,
                sfwThreshold = 0.75f,
                nsfwThreshold = 0.15f,
                contentDescription = null,
                placeholderBackgroundColor = Theme.colors.surface,
                hintText = stringResource(R.string.clear),
                textStyle = Theme.textStyle.body.small,
                iconSize = 24.dp,
            )
        },
        topLeftContent = { SaveIconChip(onClick = { /* save */ }) }
    )
}