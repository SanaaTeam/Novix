package com.sanaa.designsystem.design_system.component.chips

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun CategoryChip(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    val animateBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.secondary else Color.Transparent
    )
    val animateTextColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.onPrimary else Theme.colors.body
    )
    val animatedHorizontalPadding by animateDpAsState(
        targetValue = if (isSelected) 24.dp else 12.dp
    )
    Box(
        modifier = modifier
            .background(
                color = animateBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                onClick = onClick,
                onClickLabel = text,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(
                horizontal = animatedHorizontalPadding, vertical = 8.dp
            ), contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, style = Theme.textStyle.label.medium, color = animateTextColor
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewCategoryChip() {
    NovixTheme(isSystemInDarkTheme()) {
        var isSelected by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryChip(text = "Action", onClick = {}, isSelected = true)
            CategoryChip(
                text = "Action",
                onClick = { isSelected = !isSelected },
                isSelected = isSelected
            )
        }
    }
}