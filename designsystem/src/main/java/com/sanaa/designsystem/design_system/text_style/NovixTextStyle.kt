package com.sanaa.designsystem.design_system.text_style

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

data class NovixTextStyle(
    val headLine: SizedTextStyle,
    val title: SizedTextStyle,
    val body: SizedTextStyle,
    val label: SizedTextStyle,
)

data class SizedTextStyle(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle
)

val LocalNovixTextStyle = staticCompositionLocalOf { defaultTextStyle }