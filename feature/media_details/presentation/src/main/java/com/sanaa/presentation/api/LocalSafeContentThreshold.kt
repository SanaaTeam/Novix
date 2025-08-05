package com.sanaa.presentation.api

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSafeContentThreshold = staticCompositionLocalOf<Float> {
    error("No threshold provided")
}