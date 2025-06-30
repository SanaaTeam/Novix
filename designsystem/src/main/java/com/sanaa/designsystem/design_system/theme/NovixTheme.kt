package com.sanaa.designsystem.design_system.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.sanaa.designsystem.design_system.color.LocalNovixColors
import com.sanaa.designsystem.design_system.color.darkSchemaColors
import com.sanaa.designsystem.design_system.color.lightSchemaColors
import com.sanaa.designsystem.design_system.text_style.LocalNovixTextStyle
import com.sanaa.designsystem.design_system.text_style.defaultTextStyle

@Composable
fun NovixTheme(
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) darkSchemaColors else lightSchemaColors
    CompositionLocalProvider(
        LocalNovixColors provides colorScheme,
        LocalNovixTextStyle provides defaultTextStyle,
    ) {
        content()
    }
}