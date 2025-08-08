package com.sanaa.presentation.app

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.nav_bar.NavBar
import com.sanaa.designsystem.design_system.component.nav_bar.NavBarItem
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.presentation.api.navigation.HomeScreenRoute
import com.sanaa.presentation.api.navigation.LocalMainNavController
import com.sanaa.presentation.api.navigation.MainScreenRoutes
import com.sanaa.presentation.api.navigation.PlayListScreenRoute
import com.sanaa.presentation.api.navigation.SavedContentScreenRoute
import com.sanaa.presentation.api.navigation.SearchScreenRoute
import com.sanaa.presentation.api.navigation.UserProfileScreenRoute
import com.sanaa.presentation.navigation.HomeApiEntryPoint
import com.sanaa.presentation.screen.homeScreen.HomeScreen
import dagger.hilt.android.EntryPointAccessors

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val appContext = LocalContext.current.applicationContext

    val searchFeatureApi: SearchFeatureApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .searchApi()
    }

    val userProfileApi: UserProfileFeatureApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .userProfileApi()
    }


    CompositionLocalProvider(
        LocalMainNavController provides navController,
    ) {
        NovixScaffold(
            bottomBar = {
                AppBottomNavBar(
                    navController = navController
                )
            }, modifier = Modifier.navigationBarsPadding()
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeScreenRoute,
                modifier = Modifier
            ) {
                composable<HomeScreenRoute> {
                    HomeScreen()
                }
                composable<SearchScreenRoute> {
                    searchFeatureApi.SearchScreenApi()
                }
                composable<PlayListScreenRoute> {

                }
                composable<SavedContentScreenRoute> {

                }
                composable<UserProfileScreenRoute> {
                    userProfileApi.UserProfileScreenApi()
                }
            }
        }
    }
}

@Composable
private fun AppBottomNavBar(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Playlists,
        BottomNavItem.Saved,
        BottomNavItem.Profile
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavBar {
        navItems.forEach { item ->
            NavBarItem(
                modifier = Modifier.weight(1f),
                isSelected = currentDestination?.hasRoute(item.route::class) == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                iconRes = item.icon,
                selectedIconRes = item.selectedIcon,
            )
        }
    }
}

private sealed class BottomNavItem(
    val route: MainScreenRoutes, val icon: Int, val selectedIcon: Int
) {
    object Home :
        BottomNavItem(HomeScreenRoute, R.drawable.icon_home, R.drawable.icon_home_selected)

    object Search :
        BottomNavItem(SearchScreenRoute, R.drawable.icon_search, R.drawable.icon_search_selected)

    object Playlists : BottomNavItem(
        PlayListScreenRoute, R.drawable.icon_category, R.drawable.icon_category_selected
    )

    object Saved :
        BottomNavItem(SavedContentScreenRoute, R.drawable.icon_save_unselected, R.drawable.icon_save_selected)

    object Profile : BottomNavItem(
        UserProfileScreenRoute, R.drawable.icon_account, R.drawable.icon_account_selected
    )
}