package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.sanaa.designsystem.R

@Composable
fun RemoteImagePlaceholder(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme)
        R.drawable.icon_placeholder_dark
    else
        R.drawable.icon_placeholder_light
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = placeholderResId),
            contentDescription = "Remote Image Placeholder",
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.None
        )
    }
}

@PreviewLightDark
@Composable
fun RemoteImagePlaceholderPreview() {
    RemoteImagePlaceholder()
}


