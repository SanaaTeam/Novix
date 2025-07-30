package com.sanaa.designsystem.design_system.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.sanaa.designsystem.design_system.component.modifiers.innerShadow
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NovixPrimaryButton(
    text: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    icon: Painter? = null,
    iconTint: Color = Theme.colors.onPrimary,
    backgroundColor: Color = Theme.colors.primary,
    textColor: Color = Theme.colors.onPrimary
) {
    val animateTextColor by animateColorAsState(
        targetValue = if (isEnabled) textColor else Theme.colors.onPrimaryHint
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isEnabled) backgroundColor else Theme.colors.disable,
    )
    val animatedShadowColor by animateColorAsState(
        targetValue = if (isEnabled) Color(0x1F0D0608) else Color.Transparent
    )

    ButtonContainer(
        modifier = modifier
            .then(
                if (isEnabled)
                    Modifier.innerShadow(
                        shape = RoundedCornerShape(12.dp),
                        color = animatedShadowColor,
                        blur = 12.dp,
                        offsetX = 2.dp,
                        offsetY = 4.dp,
                    )
                else Modifier
            ),
        isLoading = isLoading,
        isEnabled = isEnabled,
        backgroundColor = animatedBackgroundColor,
        icon = icon,
        iconTint = iconTint,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        text?.let {
            Text(
                text = it,
                color = animateTextColor,
                style = Theme.textStyle.label.large
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PrimaryButtonPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NovixPrimaryButton(text = "Watch", onClick = {}, isLoading = false, modifier = Modifier.fillMaxWidth())
            NovixPrimaryButton(text = "Watch", onClick = {}, isLoading = true, modifier = Modifier.fillMaxWidth())
            NovixPrimaryButton(text = "Watch", onClick = {}, isLoading = false, isEnabled = false)
            NovixPrimaryButton(
                text = null,
                onClick = {},
                icon = painterResource(R.drawable.icon_plus)
            )
            NovixPrimaryButton(
                text = "Watch",
                onClick = {},
                icon = painterResource(R.drawable.icon_plus)
            )
        }
    }
}