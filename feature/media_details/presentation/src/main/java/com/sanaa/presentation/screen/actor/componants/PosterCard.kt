package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.mediadetails.presentation.R
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.presentation.api.LocalSafeContentThreshold
import com.sanaa.presentation.shared_component.RemoteImagePlaceholder
import com.sanaa.presentation.shared_component.cards.MediaPosterCard
import com.sanaa.presentation.shared_component.cards.SaveIconChip

@Composable
fun PosterCard(
    imageUrl: String?,
    onCardClick: () -> Unit = {},
    topLeftContent: @Composable () -> Unit = {},
) {

    MediaPosterCard(
        onCardClick = onCardClick,
        modifier = Modifier
            .width(158.dp)
            .height(210.dp),
        posterImage = {
            RemoteBlurredSensitiveImage(
                imageUrl = imageUrl ?: "",
                modifier = Modifier.fillMaxWidth(),
                sensitiveContentThreshold = 0.2f,
                isBlurEnabled = LocalSafeContentThreshold.current != 0f,
                safeContentThreshold = LocalSafeContentThreshold.current,
                contentDescription = null,
                placeholderContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
                errorContent = {
                    RemoteImagePlaceholder(Modifier.fillMaxSize())
                },
            ) {
                OnBlurContent(
                    hintText = stringResource(R.string.unsuitable_image),
                    textStyle = Theme.textStyle.body.small.copy(
                        color = Color(0x99FFFFFF)
                    ),
                    iconSize = 24.dp,
                    icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
                )
            }
        },
        topLeftContent = { topLeftContent() }
    )
}