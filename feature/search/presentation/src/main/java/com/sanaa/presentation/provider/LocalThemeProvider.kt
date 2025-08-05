package com.sanaa.presentation.provider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeProvider = staticCompositionLocalOf<Boolean> {
    error("No theme provider found")
}