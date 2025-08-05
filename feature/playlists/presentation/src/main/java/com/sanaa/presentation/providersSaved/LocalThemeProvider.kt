package com.sanaa.presentation.providersSaved

import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeProvider = staticCompositionLocalOf<Boolean> {
    error("Local Theme Provider not provided")
}