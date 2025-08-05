package com.sanaa.presentation.api

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeProvider = staticCompositionLocalOf<Boolean> {
    error("No theme provided")
}