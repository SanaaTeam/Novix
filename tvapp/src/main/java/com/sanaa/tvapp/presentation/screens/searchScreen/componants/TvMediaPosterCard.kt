package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.blur.OnBlurContent
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.image_viewer.component.RemoteBlurredSensitiveImage
import com.sanaa.tvapp.presentation.api.LocalSafeContentThreshold

@Composable
fun TvMediaPosterCard(
    title: String = "",
    imageUrl:String="",
    onCardClick: () -> Unit = {},
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
            isBlurEnabled = LocalSafeContentThreshold.current != 0f,
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

