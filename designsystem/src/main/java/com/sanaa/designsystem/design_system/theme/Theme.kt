package com.sanaa.designsystem.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.sanaa.designsystem.design_system.color.LocalNovixColors
import com.sanaa.designsystem.design_system.color.NovixColors
import com.sanaa.designsystem.design_system.text_style.LocalNovixTextStyle
import com.sanaa.designsystem.design_system.text_style.NovixTextStyle

object Theme {
    val colors: NovixColors
        @Composable @ReadOnlyComposable get() = LocalNovixColors.current
    val textStyle: NovixTextStyle
        @Composable @ReadOnlyComposable get() = LocalNovixTextStyle.current
}