package com.sanaa.presentation.providersSaved

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSafeContentThreshold = staticCompositionLocalOf<Float> {
    error("No SafeContentThreshold provided")
}