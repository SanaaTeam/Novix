package com.sanaa.presentation.api

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.HomeFeatureApi
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBar
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBarItem
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.HomeScreen
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeFeatureApiImpl : HomeFeatureApi, KoinComponent {

    private val searchFeatureApi: SearchFeatureApi by inject()
    private val playlistsFeatureApi: PlaylistsFeatureApi by inject()
    private val userProfileFeatureApi: UserProfileFeatureApi by inject()

    @Composable
    override fun HomeScreenApi() {
        val navController = rememberNavController()
        NovixTheme(isSystemInDarkTheme()) {
            NovixScaffold(
                bottomBar = {
                    AppBottomNavBar(navController = navController)
                }

            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "home",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("home") {
                        HomeScreen()
                    }
                    composable("search") {
                        searchFeatureApi.SearchScreenApi()
                    }
                    composable("playlists") {
                        playlistsFeatureApi.PlaylistsScreenApi()
                    }
                    composable("profile") {
                        userProfileFeatureApi.UserProfileScreenApi()
                    }
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

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NovixNavBar {
        navItems.forEach { item ->
            NovixNavBarItem(
                modifier = Modifier.weight(1f),
                isSelected = currentRoute == item.route,
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
    val route: String,
    val icon: Int,
    val selectedIcon: Int
) {
    object Home : BottomNavItem("home", R.drawable.icon_home, R.drawable.icon_home_selected)
    object Search :
        BottomNavItem("search", R.drawable.icon_empty_search, R.drawable.icon_search_selected)

    object Playlists :
        BottomNavItem("playlists", R.drawable.icon_category, R.drawable.icon_category_selected)

    object Saved : BottomNavItem("saved", R.drawable.icon_save, R.drawable.icon_save_selected)
    object Profile :
        BottomNavItem("profile", R.drawable.icon_account, R.drawable.icon_account_selected)
}