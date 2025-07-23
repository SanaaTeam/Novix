package com.sanaa.designsystem.design_system.theme.color

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

internal val darkSchemaColors = NovixColors(
    primary = Color(0xFFC65A42),
    secondary = Color(0xFF4B0412),
    primaryVariant = Color(0xFF1F0E0A),

    title = Color(0xDEFFFFFF),
    body = Color(0x99FFFFFF),
    hint = Color(0x61FFFFFF),
    stroke = Color(0x14FFFFFF),

    surface = Color(0xFF0D0608),
    surfaceHigh = Color(0xFF110E0F),
    onPrimary = Color(0xDEFFFFFF),
    onPrimaryHint = Color(0x61FFFFFF),
    iconBackground = Color(0xB2000000),
    iconBackgroundLow = Color(0x1FFFFFFF),
    backgroundLow = Color(0x08FFFFFF),
    disable = Color(0xFF1F1C1B),

    moviesCardGradient = Brush.verticalGradient(colors = listOf(Color(0xFFC65A42), Color(0xFF4D1D12))),
    tvShowCardGradient = Brush.verticalGradient(colors = listOf(Color(0xFF4B0412), Color(0xFF39010C))),
    peopleCardGradient = Brush.verticalGradient(colors = listOf(Color(0xFF3B99AC), Color(0xFF094E5C))),

    statusColors = StatusColors(
        redAccent = Color(0xFFF75662),
        greenAccent = Color(0xFF19744D),
        greenVariant = Color(0xFF1D1F1E),
        yellowAccent = Color(0xFFCFC657),
    )
)