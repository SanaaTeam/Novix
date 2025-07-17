package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.R

@Composable
fun GalleryCard(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }

    Box(
        modifier = modifier
            .size(88.dp)
            .border(
                BorderStroke(1.dp, Theme.colors.stroke),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ) {

        RemoteBlurredHaramImageViewer(
            imageUrl = imageUrl ?: "",
            modifier = Modifier.fillMaxWidth(),
            blurRadius = 150,
            placeholder = painterResource(placeholderResId),
            error = painterResource(placeholderResId),
            contentDescription = stringResource(com.sanaa.designsystem.R.string.movies),
            haramThreshold = 0.7f,
        )
    }
}