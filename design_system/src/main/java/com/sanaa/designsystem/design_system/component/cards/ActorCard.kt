package com.sanaa.designsystem.design_system.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun ActorCard(
    actorName: String,
    actorImage: Painter,
    modifier: Modifier = Modifier,
    playedCharacter: String? = null,
) {
    val isDarkTheme = isSystemInDarkTheme()
    val placeholderResId = if (isDarkTheme) {
        R.drawable.icon_placeholder_dark
    } else {
        R.drawable.icon_placeholder_light
    }
    Box(
        modifier = modifier
            .height(78.dp)
            .fillMaxWidth()
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


            Image(
                painter = painterResource(placeholderResId),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)

            )
            Image(
                painter = actorImage,
                contentDescription = actorName,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
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
            Text(
                text = actorName,
                style = Theme.textStyle.title.medium,
                color = Theme.colors.body,
            )
            playedCharacter?.let {
                Text(
                    text = it,
                    style = Theme.textStyle.label.small,
                    color = Theme.colors.hint
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
                actorImage = painterResource(R.drawable.icon_placeholder_light),
                playedCharacter = "Peter Parker"
            )
            ActorCard(
                actorName = "Lee Jung-jae",
                actorImage = painterResource(R.drawable.icon_placeholder_light),
                playedCharacter = null
            )
        }
    }
}