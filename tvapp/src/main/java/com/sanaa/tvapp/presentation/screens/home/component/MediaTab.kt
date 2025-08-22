package com.sanaa.tvapp.presentation.screens.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeMoviesTapRoute
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeTvShowsTapRoute
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import com.sanaa.designsystem.R as dosingSystemResource


data class MediaTabItem(
    val title: String,
    val onFocus: () -> Unit,
)

@Composable
fun MediaTab(
    sidePaddings: Dp,
    navController: NavHostController,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val layoutDirection = LocalLayoutDirection.current

    var selectedTab by remember {
        mutableIntStateOf(0)
    }

    val tabs = listOf(
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.movies),
            onFocus = { navController.navigate(HomeMoviesTapRoute) }
        ),
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.tv_shows),
            onFocus = { navController.navigate(HomeTvShowsTapRoute) }
        ),
    )

    Box(
        modifier = Modifier
            .focusable(enabled = true, interactionSource)
            .padding(top = 12.dp, start = sidePaddings, end = sidePaddings)
            .handleDPadKeyEvents(
                onLeft = {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        if (selectedTab != 0) {
                            selectedTab--
                            tabs[selectedTab].onFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                    } else {
                        if (selectedTab != tabs.lastIndex) {
                            selectedTab++
                            tabs[selectedTab].onFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    }
                },
                onRight = {
                    if (layoutDirection == LayoutDirection.Ltr) {
                        if (selectedTab != tabs.lastIndex) {
                            selectedTab++
                            tabs[selectedTab].onFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    } else {
                        if (selectedTab != 0) {
                            selectedTab--
                            tabs[selectedTab].onFocus()
                        } else {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                    }
                }
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tabs.forEachIndexed { index, tabItem ->
                Column {
                    val isSelected = selectedTab == index
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(
                                if (isSelected)
                                    Theme.colors.secondary
                                else Theme.colors.surface
                            )
                            .then(
                                if (isFocused && isSelected)
                                    Modifier.border(
                                        2.dp,
                                        Theme.colors.primary,
                                        RoundedCornerShape(100.dp)
                                    ) else Modifier
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        text = tabItem.title,
                        color = if (isSelected) Theme.colors.onPrimary else Theme.colors.title,
                        style = Theme.textStyle.title.medium
                    )
                }
            }
        }
    }
}