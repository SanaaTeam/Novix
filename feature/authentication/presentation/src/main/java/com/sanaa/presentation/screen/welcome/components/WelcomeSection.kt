 package com.sanaa.presentation.screen.welcome.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.feature.authentication.presentation.R

@Composable
fun WelcomeSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_onboarding_background),
            contentDescription = stringResource(R.string.onboarding_background),
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.00f to Color.Transparent,
                            0.20f to Theme.colors.surface.copy(alpha = 0.10f),
                            0.45f to Theme.colors.surface.copy(alpha = 0.35f),
                            0.75f to Theme.colors.surface.copy(alpha = 0.70f),
                            0.90f to Theme.colors.surface.copy(alpha = 0.85f),
                            1.00f to Theme.colors.surface.copy(alpha = 1f),
                        )
                    )
                )
        )
        Image(
            painter = painterResource(id = R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 32.dp)
        )
    }
}