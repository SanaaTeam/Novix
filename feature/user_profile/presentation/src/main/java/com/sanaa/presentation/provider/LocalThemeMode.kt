package com.sanaa.presentation.provider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeMode = staticCompositionLocalOf<Boolean> {
    error(
        "No theme found"
    )
}