package com.sanaa.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun InfoSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(Theme.colors.surface, RoundedCornerShape(16.dp))
            .border(1.dp, Theme.colors.stroke, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = title,
                style = Theme.textStyle.title.medium,
                color = Theme.colors.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
            )

            content()
        }
    }
}