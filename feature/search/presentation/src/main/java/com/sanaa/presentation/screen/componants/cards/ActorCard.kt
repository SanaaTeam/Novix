package com.sanaa.presentation.screen.componants.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredHaramImageViewer
import com.sanaa.presentation.screen.componants.RemoteImagePlaceholder

@Composable
fun ActorCard(
    actorName: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {},
    playedCharacter: String? = null,
) {
    Box(
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onCardClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(78.dp)
                .clip(
                    RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        topStart = 12.dp,
                    )
                )
                .background(Theme.colors.surface)
                .border(
                    width = 1.dp,
                    color = Theme.colors.stroke,
                    shape = RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomStart = 12.dp,
                        topStart = 12.dp,
                    )
                )
                .zIndex(10f),
            contentAlignment = Alignment.Center
        ) {
            RemoteBlurredHaramImageViewer(
                imageUrl = imageUrl,
                modifier = Modifier.fillMaxWidth(),
                haramThreshold = 0.2f,
                nonHaramThreshold = 0.7f,
                contentDescription = actorName,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .align(
                    Alignment.BottomStart
                )
                .background(
                    color = Theme.colors.surface,
                    shape = RoundedCornerShape(
                        topEnd = 12.dp, bottomEnd = 12.dp, bottomStart = 12.dp
                    )
                )
                .border(
                    width = 1.dp,
                    color = Theme.colors.stroke,
                    shape = RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomEnd = 12.dp,
                        bottomStart = 12.dp
                    )
                )
                .padding(start = 86.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BasicText(
                text = actorName,
                style = Theme.textStyle.title.medium.copy(color = Theme.colors.body),

                )
            playedCharacter?.let {
                BasicText(
                    text = it,
                    style = Theme.textStyle.label.small.copy(color = Theme.colors.hint),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewActorCard() {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ActorCard(
                actorName = "Lee Jung-jae",
                imageUrl = "",
                playedCharacter = "Peter Parker"
            )
            ActorCard(
                actorName = "Lee Jung-jae",
                imageUrl = "",
                playedCharacter = null
            )
        }
    }
}