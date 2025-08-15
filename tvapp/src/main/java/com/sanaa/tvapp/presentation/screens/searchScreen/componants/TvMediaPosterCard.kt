package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage

@Composable
fun TvMediaPosterCard(
    modifier: Modifier = Modifier,
    title: String = "",
    imageUrl:String="",
    onCardClick: () -> Unit = {},
//    topLeftContent: @Composable () -> Unit = {},
//    posterImage: @Composable () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(153.dp)
            .height(231.dp),
        onClick = {
            onCardClick()
        },
        colors = CardDefaults.colors(),
        border = CardDefaults.border(
            border = Border.None,
            focusedBorder = Border(
                border = BorderStroke(
                    width = 3.dp,
                    color = Theme.colors.primary,
                ),
                shape = RoundedCornerShape(12.dp),
            ),
        ),
        scale = CardDefaults.scale(focusedScale = 1.05f),
    ) {
        RemoteBlurredSensitiveImage(
            imageUrl = imageUrl,
            contentDescription = title
        ) {
            OnBlurContent(
                hintText = stringResource(R.string.unsuitable_image),
                textStyle = Theme.textStyle.body.small.copy(
                    color = Color(0x99FFFFFF)
                ),
                iconSize = 24.dp,
                icon = painterResource(R.drawable.icon_eye_slash),
            )
        }
    }
}

