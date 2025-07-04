package com.sanaa.designsystem.design_system.component.carousel

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NovixCarouselDots(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onDotClick: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedIndex
            val animatedBackgroundColor by animateColorAsState(
                targetValue = if (isSelected) Theme.colors.primary else Theme.colors.onPrimaryHint,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
            )
            val animatedStrokeColor by animateColorAsState(
                targetValue = if (isSelected) Color.Transparent else Theme.colors.stroke,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
            )
            val animatedDotSize by animateDpAsState(
                targetValue = if (isSelected) 8.dp else 5.dp,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessHigh
                ),
            )

            Box(
                modifier = Modifier
                    .size(animatedDotSize)
                    .clip(CircleShape)
                    .background(
                        color = animatedBackgroundColor
                    )
                    .border(
                        width = 0.5.dp,
                        color = animatedStrokeColor,
                        shape = CircleShape
                    )
                    .clickable(
                        enabled = !isSelected,
                        onClick = { onDotClick(index) }
                    )
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewNovixCarouselDots() {
    NovixTheme(isDarkMode = isSystemInDarkTheme()) {
        var selectedIndex by remember { mutableIntStateOf(2) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.surface)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NovixCarouselDots(
                totalDots = 10,
                selectedIndex = selectedIndex,
                onDotClick = { selectedIndex = it }
            )
        }
    }
}