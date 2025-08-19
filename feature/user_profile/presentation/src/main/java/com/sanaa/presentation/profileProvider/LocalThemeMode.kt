package com.sanaa.presentation.profileProvider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeMode = staticCompositionLocalOf<Boolean> {
    error("No theme found")
}