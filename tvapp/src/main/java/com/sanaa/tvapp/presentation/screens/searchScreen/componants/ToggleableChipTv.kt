package com.sanaa.tvapp.presentation.screens.searchScreen.componants

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun ToggleableChipTv(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    animateWidth: Boolean = true,
    selectedBackgroundColor: Color = Theme.colors.secondary,
    selectedTextColor: Color = Theme.colors.onPrimary,
    notSelectedTextColor: Color = Theme.colors.body,
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) Theme.colors.primary else Color.Transparent
    )

    val animateBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) selectedBackgroundColor else Color.Transparent
    )

    val animateTextColor by animateColorAsState(
        targetValue = if (isSelected) selectedTextColor else notSelectedTextColor
    )

    val animatedHorizontalPadding by animateDpAsState(
        targetValue = if (isSelected && animateWidth) 24.dp else 12.dp
    )

    Box(
        modifier = modifier
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .background(
                color = animateBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 3.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .height(37.dp)
            .clickable(
                onClick = onClick,
                onClickLabel = text,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = animatedHorizontalPadding, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = Theme.textStyle.label.medium,
            color = animateTextColor
        )
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(name = "Chip Not Selected", showBackground = true)
fun PreviewToggleableChip_NotSelected() {
    ToggleableChipTv(
        text = "Category",
        onClick = {},
        isSelected = false
    )
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(name = "Chip Selected", showBackground = true)
fun PreviewToggleableChip_Selected() {
    ToggleableChipTv(
        text = "Category",
        onClick = {},
        isSelected = true
    )
}
