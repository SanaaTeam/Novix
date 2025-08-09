package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sanaa.tvapp.presentation.screens.home.HomeScreen
import com.sanaa.tvapp.presentation.screens.login.LoginScreenTv
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchScreen

@Composable
fun TvNavGraph(navController: NavHostController, startDestination: Any) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<TvAppRoute.Home> { HomeScreen() }
        composable<TvAppRoute.Search> { SearchScreen() }
        composable<TvAppRoute.Login> { LoginScreenTv(
            onFinish = {
                navController.navigate(TvAppRoute.Home) {
                    popUpTo(TvAppRoute.Login) { inclusive = true }
                }
            }
        ) }
        composable<TvAppRoute.Categories> { }
        composable<TvAppRoute.MyList> {  }
        composable<TvAppRoute.MyAccount> { }
    }
}