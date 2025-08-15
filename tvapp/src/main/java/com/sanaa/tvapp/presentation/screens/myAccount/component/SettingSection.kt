package com.sanaa.tvapp.presentation.screens.myAccount.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun SettingSection(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Theme.colors.surfaceHigh)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier,
            text = title,
            color = Theme.colors.title,
            style = Theme.textStyle.title.medium
        )

        Row {
            content()
        }
    }
}

@Composable
fun SettingButton() {
    
}

@PreviewLightDark
@Composable
private fun SettingSectionPreview() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        SettingSection("Settings") {
            Text(
                modifier = Modifier,
                text = "Hi",
                color = Theme.colors.title,
                style = Theme.textStyle.title.medium
            )

            Text(
                modifier = Modifier,
                text = "Hi",
                color = Theme.colors.title,
                style = Theme.textStyle.title.medium
            )
        }
    }
}