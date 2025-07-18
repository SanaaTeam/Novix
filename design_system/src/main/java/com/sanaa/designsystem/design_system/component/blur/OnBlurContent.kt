package com.sanaa.designsystem.design_system.component.blur

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun OnBlurContent(
    icon: Painter,
    hintText: String,
    textStyle: TextStyle,
    iconSize: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x52000000)),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        Image(
            painter = icon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(Color(0x99FFFFFF)),
        )

        Text(
            text = hintText,
            style = textStyle,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewOnBlurContent() {
    NovixTheme(isSystemInDarkTheme()) {
        OnBlurContent(
            hintText = stringResource(R.string.unsuitable_image),
            textStyle = Theme.textStyle.body.small.copy(
                color = Color(0x99FFFFFF)
            ),
            iconSize = 24.dp,
            icon = painterResource(com.sanaa.designsystem.R.drawable.icon_eye_slash),
        )
    }
}