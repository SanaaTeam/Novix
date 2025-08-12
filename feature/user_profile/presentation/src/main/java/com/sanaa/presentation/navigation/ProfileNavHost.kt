package com.sanaa.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.component.api.LocalBottomBarVisibility
import com.sanaa.presentation.MyProfileViewModel
import com.sanaa.presentation.profileProvider.LocalSaveContentThreshold
import com.sanaa.presentation.provider.LocalNavControllerProvider
import com.sanaa.presentation.provider.LocalThemeMode
import com.sanaa.presentation.screen.changePassword.ChangePasswordWebView
import com.sanaa.presentation.screen.myAccount.MyAccountScreen
import com.sanaa.presentation.screen.myRating.MyRatingScreen
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreen

@Composable
fun AccountNavHost(
    resetKey: Any = Unit,
    viewModel: MyProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    key(resetKey) {
        val appNavController = rememberNavController()
        val bottomBarVisible = LocalBottomBarVisibility.current
        val navBackStackEntry by appNavController.currentBackStackEntryAsState()

        LaunchedEffect(navBackStackEntry) {
            bottomBarVisible.value =
                when (navBackStackEntry?.destination?.route) {
                    MyAccountScreenRoute::class.qualifiedName -> true
                    else -> false
                }
        }

        CompositionLocalProvider(
            LocalNavControllerProvider provides appNavController,
            LocalThemeMode provides state.value.isDarkTheme,
            LocalSaveContentThreshold provides state.value.safeContentThreshold
        ) {
            NavHost(
                navController = appNavController,
                startDestination = MyAccountScreenRoute,
            ) {
                composable<MyAccountScreenRoute> {
                    MyAccountScreen()
                }

                composable<MyRatingScreenRoute> {
                    MyRatingScreen()
                }

                composable(ChangePasswordScreenRoute::class) {
                    ChangePasswordWebView(url = "https://www.themoviedb.org/settings/profile")
                }

                composable<WatchingHistoryScreenRoute> {
                    WatchingHistoryScreen()
                }
            }
        }
    }
}