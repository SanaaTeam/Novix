package com.sanaa.tvapp.presentation.api

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSafeContentThreshold = staticCompositionLocalOf<Float> {
    error("No threshold provided")
}