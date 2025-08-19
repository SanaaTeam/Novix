package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalAppNavController =
    staticCompositionLocalOf<NavHostController> {
        error("LocalAppNavController not provided. Did you forget to set it up with CompositionLocalProvider?")
    }