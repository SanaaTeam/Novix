
package com.sanaa.tvapp.presentation.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.NavigationDrawerItemDefaults
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme

@Composable
fun TvNavigation(content: @Composable () -> Unit) {
    val navController = LocalAppNavController.current
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val screens = listOf(
        NavBarRoute.Home,
        NavBarRoute.Search,
        NavBarRoute.Categories,
        NavBarRoute.MyList,
        NavBarRoute.MyAccount
    )

    NavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(color = Theme.colors.surface)
                    .fillMaxHeight()
                    .padding(start = 12.dp, end = 12.dp, top = 133.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                screens.forEach { screen ->
                    val isSelected = currentRoute == screen::class.qualifiedName
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            containerColor = Theme.colors.surface,
                            contentColor = Theme.colors.title,
                            focusedContainerColor = Theme.colors.stroke,
                            focusedContentColor = Theme.colors.title,
                            selectedContainerColor = Theme.colors.primary,
                            selectedContentColor = Theme.colors.onPrimary
                        ),
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen) {
                                popUpTo<NavBarRoute.Home>()
                                launchSingleTop = true
                            }
                        },
                        leadingContent = {
                            NavBarItem(
                                modifier = Modifier.weight(1f),
                                isSelected = isSelected,
                                onClick = {},
                                iconRes = screen.unselectedIcon,
                                selectedIconRes = screen.selectedIcon,
                            )
                        }
                    ) {
                        Text(text = stringResource(id = screen.titleRes))
                    }
                }
            }
        },
        content = content
    )
}