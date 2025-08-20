package com.sanaa.presentation.profileProvider

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavControllerProvider = staticCompositionLocalOf<NavHostController> {
    error("No navigation provider found")
}