package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun NavBarItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    iconRes: Int,
    selectedIconRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ), contentAlignment = Alignment.BottomCenter
    ) {
        Crossfade(
            targetState = isSelected, animationSpec = tween(50, easing = FastOutSlowInEasing)
        ) { selected ->
            if (selected) {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .height(16.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Theme.colors.primary)
                        )

                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(42.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = selectedIconRes),
                            contentDescription = null,
                            tint = LocalContentColor.current,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}