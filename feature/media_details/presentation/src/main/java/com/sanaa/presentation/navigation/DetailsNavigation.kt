package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController


val LocalNavControllerProvider = staticCompositionLocalOf<NavHostController> {
    error("No navigation provider found")
}

object DetailsNavigation {
    val controller: NavHostController
        @Composable @ReadOnlyComposable get() = LocalNavControllerProvider.current
}