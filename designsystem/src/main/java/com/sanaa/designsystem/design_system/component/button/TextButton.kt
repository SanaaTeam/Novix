package com.sanaa.designsystem.design_system.component.button

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.common.AnimatedLoadingIndicator
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Theme.colors.primary,
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {
    val animatedTextColor by animateColorAsState(
        targetValue = if (isEnabled) textColor else Theme.colors.disable
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = Theme.textStyle.label.medium,
            modifier = modifier.clickable(
                enabled = isEnabled && !isLoading,
                onClick = onClick
            ),
            color = animatedTextColor
        )
        Crossfade(isLoading) { loading ->
            if (loading)
                AnimatedLoadingIndicator(
                    iconTint = textColor,
                    modifier = Modifier.padding(start = 4.dp)
                )
        }
    }

}

@PreviewLightDark
@Composable
private fun TextButtonPreview() {
    NovixTheme(isSystemInDarkTheme()) {
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(text = "Watch", onClick = {}, isLoading = true)
            TextButton(text = "Watch", onClick = {}, isLoading = false, isEnabled = false)
        }
    }
}