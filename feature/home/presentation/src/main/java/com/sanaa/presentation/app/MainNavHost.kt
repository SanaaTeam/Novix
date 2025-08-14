package com.sanaa.presentation.app

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sanaa.api.CategoryFeatureApi
import com.sanaa.api.PlaylistsFeatureApi
import com.sanaa.api.SearchFeatureApi
import com.sanaa.api.UserProfileFeatureApi
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.api.LocalBottomBarVisibility
import com.sanaa.designsystem.design_system.component.nav_bar.NavBar
import com.sanaa.designsystem.design_system.component.nav_bar.NavBarItem
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.presentation.api.HomeApiEntryPoint
import com.sanaa.presentation.app.navigation.AppRoute
import com.sanaa.presentation.app.navigation.CategoryScreenRoute
import com.sanaa.presentation.app.navigation.HomeScreenRoute
import com.sanaa.presentation.app.navigation.LocalMainNavController
import com.sanaa.presentation.app.navigation.PlayListScreenRoute
import com.sanaa.presentation.app.navigation.SearchScreenRoute
import com.sanaa.presentation.app.navigation.TopRatedMediaScreenRoute
import com.sanaa.presentation.app.navigation.TrendingMoviesScreenRoute
import com.sanaa.presentation.app.navigation.TrendingPeopleScreenRoute
import com.sanaa.presentation.app.navigation.TrendingTvShowsScreenRoute
import com.sanaa.presentation.app.navigation.UserProfileScreenRoute
import com.sanaa.presentation.app.navigation.WatchingMediaHistoryScreenRoute
import com.sanaa.presentation.screen.homeScreen.HomeScreen
import com.sanaa.presentation.screen.topRatingScreen.TopRatedMediaScreen
import com.sanaa.presentation.screen.trendingMoviesScreen.TrendingMoviesScreen
import com.sanaa.presentation.screen.trendingPeopleScreen.TrendingPeopleScreen
import com.sanaa.presentation.screen.trendingTvShowScreen.TrendingTvShowsScreen
import com.sanaa.presentation.screen.watchingHistoryScreen.WatchingMediaHistoryScreen
import dagger.hilt.android.EntryPointAccessors


@Composable
fun MainNavHost() {
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
    val playlistsFeatureApi: PlaylistsFeatureApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .playListApi()
    }
    val categoryFeatureApi: CategoryFeatureApi = remember {
        EntryPointAccessors
            .fromApplication(appContext, HomeApiEntryPoint::class.java)
            .categoryApi()
    }
    val navController = rememberNavController()

    val bottomBarVisible = remember { mutableStateOf(true) }


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        val currentRoute = navBackStackEntry?.destination?.route
        bottomBarVisible.value = currentRoute in setOf(
            HomeScreenRoute::class.qualifiedName,
            SearchScreenRoute::class.qualifiedName,
            PlayListScreenRoute::class.qualifiedName,
            CategoryScreenRoute::class.qualifiedName,
            UserProfileScreenRoute::class.qualifiedName
        )
    }

    NovixScaffold(
        bottomBar = {
            if (bottomBarVisible.value) {
                AppBottomNavBar(navController = navController)
            }
        },
        modifier = Modifier.navigationBarsPadding()
    ) {
        CompositionLocalProvider(
            LocalMainNavController provides navController,
            LocalBottomBarVisibility provides bottomBarVisible,
        ) {
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
                    playlistsFeatureApi.PlaylistsScreenApi()
                }
                composable<CategoryScreenRoute> {
                    categoryFeatureApi.CategoryScreenApi()
                }
                composable<UserProfileScreenRoute> {
                    userProfileApi.UserProfileScreenApi()
                }
                composable<TrendingMoviesScreenRoute> {
                    TrendingMoviesScreen()
                }
                composable<TrendingTvShowsScreenRoute> {
                    TrendingTvShowsScreen()
                }
                composable<TrendingPeopleScreenRoute> {
                    TrendingPeopleScreen()
                }
                composable<TopRatedMediaScreenRoute> {
                    TopRatedMediaScreen()
                }
                composable<WatchingMediaHistoryScreenRoute> {
                    WatchingMediaHistoryScreen()
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
        BottomNavItem.Category,
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
    val route: AppRoute, val icon: Int, val selectedIcon: Int
) {
    object Home :
        BottomNavItem(HomeScreenRoute, R.drawable.icon_home, R.drawable.icon_home_selected)

    object Search :
        BottomNavItem(SearchScreenRoute, R.drawable.icon_search, R.drawable.icon_search_selected)

    object Category : BottomNavItem(
        CategoryScreenRoute, R.drawable.icon_category, R.drawable.icon_category_selected
    )

    object Saved :
        BottomNavItem(
            PlayListScreenRoute,
            R.drawable.icon_save_unselected,
            R.drawable.icon_save_selected
        )

    object Profile : BottomNavItem(
        UserProfileScreenRoute, R.drawable.icon_account, R.drawable.icon_account_selected
    )
}