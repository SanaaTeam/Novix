package com.sanaa.designsystem.design_system.component.section_header

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun InlineAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(R.drawable.icon_right_arrow),
    text: String = stringResource(R.string.view_all),
    iconColor: Color = Theme.colors.primary,
    textColor: Color = Theme.colors.primary
) {
    Row(
        modifier = modifier.clickable(
            onClick = onClick,
            onClickLabel = text,
            indication = null,
            interactionSource = remember { MutableInteractionSource() }

        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = text,
            style = Theme.textStyle.label.medium,
            color = textColor
        )
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor,
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewViewAllIcon() {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.surfaceHigh)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InlineAction(
                onClick = {}
            )
        }
    }

}