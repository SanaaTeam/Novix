package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder

@Composable
fun GalleryCard(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {

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
            imageUrl = imageUrl.orEmpty(),
            modifier = Modifier.fillMaxWidth(),
            haramThreshold = 0.2f,
            nonHaramThreshold = 0.7f,
            contentDescription = null,
            placeholderContent = {
                RemoteImagePlaceholder(Modifier.fillMaxSize())
            },
            errorContent = {
                RemoteImagePlaceholder(Modifier.fillMaxSize())
            },
        ) {
            OnBlurContent(
                iconSize = 24.dp,
                icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
            )
        }
    }
}