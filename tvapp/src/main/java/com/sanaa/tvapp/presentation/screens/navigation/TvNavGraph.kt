package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun TvNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
        composable(Screen.Search.route) { SettingsScreen() }
        composable(Screen.Categories.route) { FavoritesScreen() }
        composable(Screen.MyList.route) { FavoritesScreen() }
        composable(Screen.MyAccount.route) { FavoritesScreen() }
    }
}
