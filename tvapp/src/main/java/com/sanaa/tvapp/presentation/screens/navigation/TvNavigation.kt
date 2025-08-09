package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.component.nav_bar.NavBarItem


@Composable
fun TvNavigation(
    navController: NavHostController, content: @Composable () -> Unit
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val screens =
        listOf(Screen.Home, Screen.Search, Screen.Categories, Screen.MyList, Screen.MyAccount)

    NavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp, top = 133.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                screens.forEach { screen ->
                    NavigationDrawerItem(selected = currentRoute == screen.route, onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }, leadingContent = {
                        NavBarItem(
                            modifier = Modifier.weight(1f),
                            isSelected = currentRoute == screen.route,
                            onClick = {},
                            iconRes = screen.icon,
                            selectedIconRes = screen.icon,
                        )
                    }) {
                        Text(text = screen.title)
                    }
                }
            }
        }
    ){
        content()
    }
}