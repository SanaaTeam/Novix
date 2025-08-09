package com.sanaa.presentation.api.navigationSaved

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController

val LocalNavControllerProvider = staticCompositionLocalOf<NavHostController> {
    error("No navigation provider found")
}
