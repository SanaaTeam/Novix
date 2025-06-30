package com.sanaa.designsystem.design_system.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.common.ButtonContainer
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun OutlinedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    textColor: Color = Theme.colors.primary,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    icon: Painter? = null,
    iconTint: Color = Theme.colors.primary
) {
    val animateTextColor by animateColorAsState(
        targetValue = if (isEnabled) textColor else Theme.colors.disable
    )
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isEnabled) Theme.colors.stroke else Theme.colors.disable,
    )
    ButtonContainer(
        modifier = modifier.border(
            width = 1.dp,
            color = animatedBorderColor,
            shape = RoundedCornerShape(12.dp)
        ),
        isLoading = isLoading,
        isEnabled = isEnabled,
        backgroundColor = Color.Transparent,
        icon = icon,
        iconTint = iconTint,
        onClick = onClick,
    ) {
        Text(
            text = text,
            color = animateTextColor,
            style = Theme.textStyle.label.large
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewOutlinedButton() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(text = "Watch", onClick = {}, isLoading = true)
            OutlinedButton(text = "Watch", onClick = {}, isLoading = false, isEnabled = false)
            OutlinedButton(
                text = "Watch",
                onClick = {},
                icon = painterResource(R.drawable.plus_icon)
            )
        }
    }
}