package com.sanaa.presentation.screen.homeScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sanaa.designsystem.design_system.theme.Theme

object AppGradients {
    val moviesCardGradientColor: Brush
        @Composable get() = Brush.verticalGradient(
            colors = if (isSystemInDarkTheme()) {
                listOf(Theme.colors.primary, Color(0xFF4D1D12))
            } else {
                listOf(Color(0xFFF77053), Color(0xFFBF4C33))
            }
        )

    val tvShowCardGradientColor: Brush
        @Composable get() = Brush.verticalGradient(
            colors = if (isSystemInDarkTheme()) {
                listOf(Color(0xFF4B0412), Color(0xFF39010C))
            } else {
                listOf(Color(0xFF80071F), Color(0xFF5B0113))
            }
        )

    val peopleCardGradientColor: Brush
        @Composable get() = Brush.verticalGradient(
            colors = if (isSystemInDarkTheme()) {
                listOf(Color(0xFF3B99AC), Color(0xFF094E5C))
            } else {
                listOf(Color(0xFF3B99AC), Color(0xFF21606D))
            }
        )
}