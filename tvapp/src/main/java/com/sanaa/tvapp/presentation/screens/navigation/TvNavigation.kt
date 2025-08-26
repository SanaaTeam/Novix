package com.sanaa.tvapp.presentation.screens.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents

@Composable
fun TvNavigation(content: @Composable () -> Unit) {
    val navController = LocalAppNavController.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val screens = listOf(
        NavBarRoute.Home,
        NavBarRoute.Search,
        NavBarRoute.Categories,
        NavBarRoute.MyAccount
    )


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isDrawerVisible = screens.any { it::class.qualifiedName == currentRoute }

    val drawerFocusRequester = remember { FocusRequester() }

    CompositionLocalProvider(LocalDrawerFocusRequester provides drawerFocusRequester) {
        NavigationDrawer(
            drawerContent = {
                AnimatedVisibility(
                    visible = isDrawerVisible,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .focusRequester(drawerFocusRequester)
                            .focusable(enabled = true, interactionSource)
                            .padding(end = 12.dp)
                            .handleDPadKeyEvents(
                                onUp = {
                                    if (selectedTab != 0) {
                                        selectedTab -= 1
                                        navController.navigate(screens[selectedTab]) {
                                            popUpTo(0)
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onDown = {
                                    if (selectedTab != screens.lastIndex) {
                                        selectedTab += 1
                                        navController.navigate(screens[selectedTab]) {
                                            popUpTo(0)
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .background(color = Theme.colors.surface)
                                .selectableGroup(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            screens.forEachIndexed { index, screen ->
                                OvalColoredShadow(
                                    hasFocus = isFocused,
                                    isSelected = selectedTab == index,
                                    screen = screen,
                                    shadowColor = Theme.colors.primary
                                )
                            }
                        }
                    }
                }
            },
            content = content
        )
    }


}

@Composable
fun OvalColoredShadow(
    hasFocus: Boolean,
    isSelected: Boolean,
    shadowColor: Color,
    screen: NavBarRoute,
    modifier: Modifier = Modifier,
    shadowRadius: Dp = 20.dp,
    shadowOffsetY: Dp = 8.dp,
) {
    val density = LocalDensity.current
    val shadowRadiusPx = with(density) { shadowRadius.toPx() }
    val offsetYPx = with(density) { shadowOffsetY.toPx() }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(42.dp)) {
            if (isSelected) {
                Box(
                    modifier = modifier
                        .matchParentSize()
                        .drawBehind {
                            getOvalShadow(shadowRadiusPx, offsetYPx, shadowColor)
                        }
                )
            }

            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
                painter = painterResource(if (isSelected) screen.selectedIcon else screen.unselectedIcon),
                contentDescription = null,
                tint = if (isSelected) Theme.colors.primary else Theme.colors.hint,
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .size(4.dp)
                        .background(Theme.colors.primary)
                )
            }
        }

        AnimatedVisibility(visible = hasFocus) {
            Text(
                text = stringResource(screen.titleRes),
                modifier = Modifier.padding(start = 4.dp, end = 12.dp),
                style = Theme.textStyle.label.medium,
                color = if (isSelected) Theme.colors.primary else Theme.colors.hint
            )
        }
    }
}

private fun DrawScope.getOvalShadow(
    shadowRadiusPx: Float,
    offsetYPx: Float,
    shadowColor: Color,
) {
    val paint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        color = android.graphics.Color.TRANSPARENT
        setShadowLayer(
            shadowRadiusPx,
            0f,
            offsetYPx,
            shadowColor.copy(alpha = 0.3f).toArgb()
        )
    }

    drawIntoCanvas { canvas ->
        val ovalWidth = size.width
        val ovalHeight = size.height * 0.8f
        val left = (size.width - ovalWidth) / 2
        val top = size.height - ovalHeight
        val right = left + ovalWidth
        val bottom = top + ovalHeight

        canvas.nativeCanvas.drawOval(
            left, top, right, bottom, paint
        )
    }
}