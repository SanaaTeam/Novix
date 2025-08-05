package com.sanaa.presentation.providers

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSafeContentThreshold = staticCompositionLocalOf<Float> {
    error("No SafeContentThreshold provided")
}