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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.screens.home.SelectedHomeTab
import com.sanaa.tvapp.presentation.screens.searchScreen.SearchTvScreenUiState
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import com.sanaa.designsystem.R as dosingSystemResource

data class MediaTabItem(
    val title: String,
    val onFocus: () -> Unit,
)

@Composable
fun HomeTabs(
    modifier: Modifier = Modifier,
    sidePaddings: Dp,
    onTabSelected: (SelectedHomeTab) -> Unit,
) {

    val tabs = listOf(
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.movies),
            onFocus = { onTabSelected(SelectedHomeTab.MOVIES) }
        ),
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.tv_shows),
            onFocus = { onTabSelected(SelectedHomeTab.TV_SHOWS) }
        ),
    )

    MediaTab(
        modifier = modifier,
        tabs = tabs,
        sidePaddings = sidePaddings,
        textStyle = Theme.textStyle.title.medium
    )
}

@Composable
fun SearchTabs(
    modifier: Modifier = Modifier,
    sidePaddings: Dp,
    onFocus: (Int) -> Unit,
) {
    val tabs = listOf(
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.movies),
            onFocus = { onFocus(SearchTvScreenUiState.MOVIE_INDEX) }
        ),
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.tv_shows),
            onFocus = { onFocus(SearchTvScreenUiState.TV_SHOW_INDEX) }
        ),
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.actors),
            onFocus = { onFocus(SearchTvScreenUiState.ACTOR_INDEX) }
        ),
    )

    MediaTab(
        modifier = modifier,
        tabs = tabs,
        sidePaddings = sidePaddings,
        textStyle = Theme.textStyle.label.medium
    )
}

@Composable
fun GenreTabs(
    sidePaddings: Dp = 0.dp,
    onFocus: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = listOf(
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.movies),
            onFocus = { onFocus(0) }
        ),
        MediaTabItem(
            title = stringResource(dosingSystemResource.string.tv_shows),
            onFocus = { onFocus(1) }
        ),
    )

    MediaTab(
        modifier = modifier,
        tabs = tabs,
        sidePaddings = sidePaddings,
        textStyle = Theme.textStyle.label.medium,
    )
}

@Composable
fun MediaTab(
    textStyle: TextStyle,
    tabs: List<MediaTabItem>,
    sidePaddings: Dp,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var alreadyFocused by remember {
        mutableStateOf(false)
    }
    val layoutDirection = LocalLayoutDirection.current

    var selectedTab by rememberSaveable {
        mutableIntStateOf(0)
    }

    LaunchedEffect(isFocused) {
        if (isFocused.not())
            alreadyFocused = isFocused
    }

    Box(
        modifier = modifier
            .focusable(enabled = true, interactionSource)
            .padding(start = sidePaddings, end = sidePaddings)
            .handleDPadKeyEvents(
                onUp = {
                    if (alreadyFocused.not()) {
                        alreadyFocused = true
                        return@handleDPadKeyEvents
                    }
                    focusManager.moveFocus(FocusDirection.Up)
                },
                onDown = {
                    if (alreadyFocused.not()) {
                        alreadyFocused = true
                        return@handleDPadKeyEvents
                    }
                    focusManager.moveFocus(FocusDirection.Down)
                },
                onLeft = {
                    if (alreadyFocused.not()) {
                        alreadyFocused = true
                        return@handleDPadKeyEvents
                    }
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
                    if (alreadyFocused.not()) {
                        alreadyFocused = true
                        return@handleDPadKeyEvents
                    }

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
                            .padding(
                                horizontal = 24.dp,
                                vertical = 8.dp
                            ),
                        text = tabItem.title,
                        color = if (isSelected) Theme.colors.onPrimary else Theme.colors.title,
                        style = textStyle,
                    )
                }
            }
        }
    }
}