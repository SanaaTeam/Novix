package com.sanaa.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.screen.changePassword.ChangePasswordWebView
import com.sanaa.presentation.screen.myAccount.MyAccountScreen

@Composable
fun ProfileNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()


    CompositionLocalProvider(LocalNavControllerProvider provides navController) {
        NovixTheme (isDarkMode = isSystemInDarkTheme() ){
            NavHost(
                navController = navController,
                startDestination = AccountMainScreenRoute,
                modifier = Modifier.background(Theme.colors.surface)
            ){
                composable(AccountMainScreenRoute::class) {
                    MyAccountScreen()
                }
                composable(WatchingHistoryScreenRoute::class) {

                }
                composable(MyRatingScreenRoute::class) {

                }

                composable(ChangPasswordRoute::class) {
                    ChangePasswordWebView(url = "https://www.themoviedb.org/settings/profile")
                }
            }
        }
    }






}