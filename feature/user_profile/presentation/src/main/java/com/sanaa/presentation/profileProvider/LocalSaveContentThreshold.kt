package com.sanaa.presentation.profileProvider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSaveContentThreshold = staticCompositionLocalOf<Float> {
    error("no content restriction provider found")
}