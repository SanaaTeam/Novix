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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

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
            .handleDPadKeyEvents(onLeft = {
                if (selectedTab != 0) {
                    selectedTab -= 1
                    tabs[selectedTab].onFocus()
                }
            }, onRight = {
                if (selectedTab != tabs.lastIndex) {
                    selectedTab += 1
                    tabs[selectedTab].onFocus()
                }
            }),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            tabs.forEachIndexed { index, tabItem ->
                Column {
                    val isSelected = selectedTab == index
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected)
                                    Theme.colors.primary.copy(alpha = if (isFocused) 0.5f else 1f)
                                else Theme.colors.surface
                            )
                            .then(
                                if (isFocused && isSelected) Modifier.border(
                                    3.dp,
                                    Theme.colors.primary,
                                    RoundedCornerShape(12.dp)
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