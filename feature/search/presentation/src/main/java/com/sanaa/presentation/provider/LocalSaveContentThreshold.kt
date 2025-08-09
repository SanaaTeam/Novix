package com.sanaa.presentation.provider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSaveContentThreshold = staticCompositionLocalOf<Float> {
    error("No SaveContentThreshold provided")
}