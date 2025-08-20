package com.sanaa.designsystem.design_system.component.selection

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun OptionCard(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    description: String? = null
) {
    val animatedBackgroundColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.primaryVariant else Color.Unspecified,
        animationSpec = tween(durationMillis = 20)    )
    val animatedStrokeColor by animateColorAsState(
        targetValue = if (isSelected) Theme.colors.primary else Theme.colors.stroke,
        animationSpec = tween(durationMillis = 20)    )
    val animatedBorderWidth by animateDpAsState(
        targetValue = if (isSelected) 1.5.dp else 1.dp,
        animationSpec = tween(durationMillis = 20)    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = animatedBackgroundColor, shape = RoundedCornerShape(12.dp))
            .border(
                width = animatedBorderWidth,
                color = animatedStrokeColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = Theme.textStyle.label.large,
            color = Theme.colors.body
        )
        description?.let {
            Text(
                text = it,
                style = Theme.textStyle.label.small,
                color = Theme.colors.hint
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewOptionCard() {
    NovixTheme(isSystemInDarkTheme()) {
        val selected = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(color = Theme.colors.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OptionCard(
                label = "Option 1",
                isSelected = selected.value,
                onClick = { selected.value = !selected.value },
            )
            OptionCard(
                label = "Option 2",
                isSelected = false,
                onClick = {},
            )
        }
    }
}