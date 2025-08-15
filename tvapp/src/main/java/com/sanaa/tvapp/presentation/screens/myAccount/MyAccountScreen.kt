package com.sanaa.tvapp.presentation.screens.myAccount

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Text
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.tvapp.presentation.components.MediaSection
import com.sanaa.tvapp.presentation.screens.home.ImageList
import com.sanaa.tvapp.presentation.screens.home.component.MediaTabItem
import com.sanaa.tvapp.presentation.screens.home.component.Title
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeMoviesTapRoute
import com.sanaa.tvapp.presentation.screens.home.tabRoutes.HomeTvShowsTapRoute
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToChangePasswordSetting
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToLogin
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToMyRating
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.NavigateToWatchingHistory
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.PopBackStackToWelcomeScreen
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.UpdateAppTheme
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingSection
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.ChangePasswordScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MyRatingScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.WatchingHistoryScreenRoute
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import kotlinx.coroutines.flow.collectLatest
import com.sanaa.designsystem.R as designSystemResource
import com.sanaa.tvapp.R as tvResource

@Composable
fun MyAccountScreen(viewModel: MyAccountScreenViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = LocalAppNavController.current
    val view = LocalView.current
    val activity = view.context as? AppCompatActivity

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                NavigateToChangePasswordSetting -> {
                    navController.navigate(ChangePasswordScreenRoute)
                }

                is UpdateAppLanguage -> {
                    val localeList = LocaleListCompat.forLanguageTags(it.language)
                    if (AppCompatDelegate.getApplicationLocales()
                            .toLanguageTags() != localeList.toLanguageTags()
                    ) {
                        AppCompatDelegate.setApplicationLocales(localeList)
                    }
                    activity?.recreate()
                }

                NavigateToMyRating -> navController.navigate(MyRatingScreenRoute)
                NavigateToWatchingHistory -> navController.navigate(WatchingHistoryScreenRoute)
                is UpdateAppTheme -> {
                    if (it.isDarkMode) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                }

                NavigateToLogin -> {

                }

                PopBackStackToWelcomeScreen -> {
                    activity?.recreate()
                }
            }
        }
    }

    MyAccountScreenContent(state, viewModel)
}


@Composable
private fun MyAccountScreenContent(
    state: MyAccountScreenUiState,
    interactionsListener: MyAccountScreenInteractionsListener,
) {
    val sidePaddings = 36.dp
    val scrollState = rememberScrollState()
    val mainTvNavController = LocalAppNavController.current
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 36.dp, vertical = 24.dp)
                    .align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(tvResource.drawable.novix_logo),
                    contentDescription = null,
                )

                Text(
                    stringResource(tvResource.string.app_name),
                    color = Theme.colors.title,
                    style = Theme.textStyle.title.medium,
                )
            }
        }

        Title(
            modifier = Modifier.padding(horizontal = sidePaddings, vertical = 16.dp),
            title = stringResource(tvResource.string.settings)
        )

        SettingSection(title = stringResource(designSystemResource.string.language)) {

        }

        SettingSection(title = stringResource(designSystemResource.string.content_restriction)) {

        }

        MyAccountMediaTab(sidePaddings, navController)

        NavHost(navController = navController, startDestination = HomeMoviesTapRoute) {
            composable(route = HomeMoviesTapRoute::class) {
                MyAccountMovies(state) { id ->
                    mainTvNavController.navigate(ScreensRoute.MovieDetails(id))
                }
            }

            composable(route = HomeTvShowsTapRoute::class) {
                MyAccountMovies(state) { id ->
                    mainTvNavController.navigate(ScreensRoute.TvShowDetails(id))
                }
            }
        }
    }
}

@Composable
fun MyAccountMovies(
    state: MyAccountScreenUiState,
    onItemClick: (Int) -> Unit,
) {
    Column {
        if (state.watchingHistoryMovies.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(items = state.watchingHistoryMovies, key = { it.id }) {
                    ImageList(it, onItemClick = { id -> onItemClick(id) })
                }
            }
        }

        if (state.myRatingMovies.isNotEmpty()) {
            MediaSection(title = stringResource(tvResource.string.watching_history)) {
                items(items = state.myRatingMovies, key = { it.id }) {
                    ImageList(it, onItemClick = { id -> onItemClick(id) })
                }
            }
        }
    }
}


@Composable
fun MyAccountMediaTab(
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
            title = stringResource(designSystemResource.string.movies),
            onFocus = { navController.navigate(HomeMoviesTapRoute) }
        ),
        MediaTabItem(
            title = stringResource(designSystemResource.string.tv_shows),
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