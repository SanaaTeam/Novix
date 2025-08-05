package com.sanaa.presentation.api.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanaa.presentation.screen.myAccount.MyAccountScreen
import com.sanaa.presentation.screen.myRating.MyRatingScreen
@Composable
fun AccountNavHost() {
val appNavController = rememberNavController()

CompositionLocalProvider(LocalNavControllerProvider provides appNavController) {
    NavHost(
        navController = appNavController,
        startDestination = MyAccountScreenRoute,
    ) {
        composable<MyAccountScreenRoute>{
            MyAccountScreen()
        }

        composable<MyRatingScreenRoute> {
            MyRatingScreen()
        }

    }
}
    }