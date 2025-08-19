package com.sanaa.presentation.playListProviders

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeProvider = staticCompositionLocalOf<Boolean> { error("No theme provided") }