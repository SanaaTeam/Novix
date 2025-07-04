package com.sanaa.designsystem.design_system.component.section_header

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NovixSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    rightContent: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = Theme.textStyle.headLine.small,
            color = Theme.colors.title,
            modifier = Modifier.weight(1f)
        )
        rightContent()
    }
}

@PreviewLightDark
@Composable
private fun PreviewNovixSectionHeader() {
    NovixTheme(true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.surfaceHigh)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NovixSectionHeader(
                title = "New arrival",
                rightContent = {
                    ViewAllComponent(
                        onClick = {}
                    )
                }
            )
        }
    }
}