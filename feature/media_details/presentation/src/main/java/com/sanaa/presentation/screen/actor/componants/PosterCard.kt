package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.cards.MovieSeriesPosterCard
import com.sanaa.designsystem.design_system.component.chips.SaveIconChip
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R

@Composable
fun PosterCard(
    imageUrl: String?,
    onCardClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}

) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }

    MovieSeriesPosterCard(
        onCardClick = onCardClick,
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        boastImage = {
            RemoteBlurredHaramImageViewer(
                imageUrl = imageUrl ?: "",
                modifier = Modifier.fillMaxWidth(),
                blurRadius = 150,
                placeholder = painterResource(placeholderResId),
                error = painterResource(placeholderResId),
                contentDescription = stringResource(com.sanaa.designsystem.R.string.movies),
            )
        },
        topLeftContent = { SaveIconChip(onClick = onSaveClick) }
    )
}