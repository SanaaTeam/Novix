package com.sanaa.designsystem.design_system.component.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController


val LocalNavControllerProvider = staticCompositionLocalOf<NavHostController> {
    error("No navigation provider found. Make sure to wrap your NavHost with CompositionLocalProvider(LocalNavControllerProvider provides navController)")
} 