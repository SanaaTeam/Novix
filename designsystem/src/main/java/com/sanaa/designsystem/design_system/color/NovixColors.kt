package com.sanaa.designsystem.design_system.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class NovixColors(
    val primary: Color,
    val secondary: Color,
    val primaryVariant: Color,

    val title: Color,
    val body: Color,
    val hint: Color,
    val stroke: Color,

    val surface: Color,
    val surfaceHigh: Color,
    val onPrimary: Color,
    val onPrimaryHint: Color,
    val iconBackground: Color,
    val iconBackgroundLow: Color,
    val backgroundLow: Color,
    val disable: Color,

    val statusColors: StatusColors
)

data class StatusColors(
    val redAccent: Color,
    val greenAccent: Color,
    val yellowAccent: Color,
    val greenVariant: Color
)

val LocalNovixColors = staticCompositionLocalOf { lightSchemaColors }