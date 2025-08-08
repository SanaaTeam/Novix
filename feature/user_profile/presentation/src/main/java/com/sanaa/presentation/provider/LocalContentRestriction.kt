package com.sanaa.presentation.provider

import androidx.compose.runtime.staticCompositionLocalOf

val LocalContentRestriction = staticCompositionLocalOf<Float> {
    error("no content restriction provider found")
}