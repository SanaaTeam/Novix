package com.sanaa.designsystem.design_system.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.sanaa.designsystem.design_system.theme.color.LocalNovixColors
import com.sanaa.designsystem.design_system.theme.color.darkSchemaColors
import com.sanaa.designsystem.design_system.theme.color.lightSchemaColors
import com.sanaa.designsystem.design_system.theme.text_style.LocalNovixTextStyle
import com.sanaa.designsystem.design_system.theme.text_style.defaultTextStyle

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
        Box(modifier = Modifier.background(Theme.colors.surface)) {
            content()
        }
    }
}