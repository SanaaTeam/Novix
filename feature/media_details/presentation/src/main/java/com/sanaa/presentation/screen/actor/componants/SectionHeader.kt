package com.sanaa.presentation.screen.actor.componants

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.component.button.TextButton
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun SectionHeader(
    title: String, actionText: String, onActionClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 12.dp)
    ) {
        Text(
            text = title,
            style = Theme.textStyle.label.medium,
            color = Theme.colors.body,
            modifier = Modifier.weight(1f)
        )
        TextButton(
            text = actionText, onClick = onActionClick, isLoading = false, isEnabled = true
        )
    }
}