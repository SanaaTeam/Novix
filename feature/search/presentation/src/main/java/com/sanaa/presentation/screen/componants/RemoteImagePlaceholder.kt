package com.sanaa.presentation.screen.componants

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.presentation.provider.LocalThemeProvider

@Composable
fun RemoteImagePlaceholder(modifier: Modifier = Modifier) {
    val placeholderResId = if (LocalThemeProvider.current)
        R.drawable.icon_placeholder_dark
    else
        R.drawable.icon_placeholder_light
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = placeholderResId),
            contentDescription = "Remote Image Placeholder",
            modifier = modifier.size(56.dp),
            contentScale = ContentScale.None
        )
    }
}

@PreviewLightDark
@Composable
fun RemoteImagePlaceholderPreview() {
    RemoteImagePlaceholder()
}