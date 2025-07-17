package com.sanaa.presentation.screens.actors.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.R

@Composable
fun PosterCard(imageUrl: String?) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }

    MovieSeriesPosterCard(
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        boastImage = {
            RemoteCensoredImageViewer(
                imageUrl = imageUrl ?: "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop,
                blurRadius = 150,
                sfwThreshold = 0.75f,
                nsfwThreshold = 0.15f,
                placeholder = painterResource(placeholderResId),
                error = painterResource(placeholderResId),
                contentDescription = stringResource(com.sanaa.designsystem.R.string.movies),
                placeholderBackgroundColor = Theme.colors.surface,
                hintText = stringResource(R.string.unsuitable_image),
                textStyle = Theme.textStyle.body.small,
                iconSize = 24.dp,
            )
        },
        topLeftContent = { SaveIconChip(onClick = { /* save */ }) }
    )
}