package com.sanaa.designsystem.design_system.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NegativeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Theme.colors.statusColors.redAccent,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    icon: Painter? = null,
    iconTint: Color = Theme.colors.statusColors.redAccent
) {
    NovixOutlinedButton(
        text = text,
        modifier = modifier,
        onClick = onClick,
        isEnabled = isEnabled,
        icon = icon,
        iconTint = iconTint,
        isLoading = isLoading,
        textColor = textColor,
    )
}

@PreviewLightDark
@Composable
private fun PreviewNegativeButton() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NegativeButton(
                text = "Delete",
                onClick = { isLoading = !isLoading },
                isLoading = isLoading,
                icon = painterResource(id = R.drawable.icon_plus),
            )
        }
    }
}