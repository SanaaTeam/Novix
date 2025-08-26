package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.focus.FocusRequester

val LocalDrawerFocusRequester = staticCompositionLocalOf<FocusRequester> {
    error("No DrawerFocusRequester provided")
}