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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteCensoredImageViewer
import com.sanaa.presentation.R

@Composable
fun GalleryCard(
    imageUrl: String?,
    modifier: Modifier = Modifier.size(88.dp)
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.movie_placeholder_dark
    } else {
        R.drawable.movie_placeholder_light
    }

    Box(
        modifier = modifier
            .border(
                BorderStroke(1.dp, Theme.colors.stroke),
                RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
    ) {

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
            hintText = stringResource(R.string.show),
            textStyle = Theme.textStyle.body.small,
            iconSize = 24.dp,
        )
    }
}