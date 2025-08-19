package com.sanaa.presentation.app

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.providers.LocalSafeContentThreshold
import com.sanaa.presentation.providers.LocalThemeProvider

@Composable
fun NovixApp(
    viewModel: NovixAppViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val view = LocalView.current
    val navColor = Theme.colors.surface
    val activity = view.context as? ComponentActivity

    LaunchedEffect(state.value.isDarkTheme) {
        activity?.window?.also { window ->
            WindowInsetsControllerCompat(window, view).apply {
                window.navigationBarColor = navColor.toArgb()
                isAppearanceLightStatusBars = !state.value.isDarkTheme
                isAppearanceLightNavigationBars = !state.value.isDarkTheme
            }
        }
    }
    CompositionLocalProvider(
        LocalSafeContentThreshold provides state.value.safeContentThreshold,
        LocalThemeProvider provides state.value.isDarkTheme
    ) {
        MainNavHost()
    }
}



