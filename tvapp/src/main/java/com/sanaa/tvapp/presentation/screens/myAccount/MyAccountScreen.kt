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
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.Recreate
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenEffect.UpdateAppLanguage
import com.sanaa.tvapp.presentation.screens.myAccount.MyAccountScreenUiState.ContentRestrictionUiState
import com.sanaa.tvapp.presentation.screens.myAccount.component.MyAccountUserInfo
import com.sanaa.tvapp.presentation.screens.myAccount.component.NotLoggedInStateComponent
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingOptionItem
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingOptions
import com.sanaa.tvapp.presentation.screens.myAccount.component.SettingSection
import com.sanaa.tvapp.presentation.screens.navigation.LocalAppNavController
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.ChangePasswordScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.MyRatingScreenRoute
import com.sanaa.tvapp.presentation.screens.navigation.ScreensRoute.WatchingHistoryScreenRoute
import com.sanaa.tvapp.util.modifier.handleDPadKeyEvents
import kotlinx.coroutines.flow.collectLatest
import repository.Language
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
                    val isLanguageSelected = AppCompatDelegate
                        .getApplicationLocales()
                        .toLanguageTags() != localeList.toLanguageTags()
                    if (isLanguageSelected) {
                        AppCompatDelegate.setApplicationLocales(localeList)
                    }
                }

                NavigateToMyRating -> {
                    navController.navigate(MyRatingScreenRoute)
                }

                NavigateToWatchingHistory -> {
                    navController.navigate(WatchingHistoryScreenRoute)
                }

                NavigateToLogin -> {
                }

                Recreate -> {
                     activity?.recreate()
                }
            }
        }
    }

    if (!state.isUserLoggedIn) {
        NotLoggedInStateComponent(state, viewModel)
    } else {
        MyAccountScreenContent(state, viewModel)
    }
}


@Composable
private fun MyAccountScreenContent(
    state: MyAccountScreenUiState,
    interactionsListener: MyAccountScreenInteractionsListener,
) {
    val scrollState = rememberScrollState()
    val mainTvNavController = LocalAppNavController.current
    val navController = rememberNavController()

    Column(
        modifier = Modifier
            .padding(horizontal = 36.dp, vertical = 24.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            MyAccountUserInfo(
                state.currentUser,
                onLogoutClick = {
                    interactionsListener.onLogoutButtonClick()
                }
            )

            Row(
                modifier = Modifier
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
            modifier = Modifier,
            title = stringResource(tvResource.string.settings)
        )

        SettingSection(
            modifier = Modifier,
            title = stringResource(designSystemResource.string.language),
        ) {
            SettingOptions(
                settingOptionItems = listOf(
                    SettingOptionItem(
                        title = stringResource(designSystemResource.string.english),
                        tag = Language.ENGLISH,
                        isSelected = state.selectedLanguage == Language.ENGLISH.code
                    ),
                    SettingOptionItem(
                        title = stringResource(designSystemResource.string.arabic),
                        tag = Language.ARABIC,
                        isSelected = state.selectedLanguage == Language.ARABIC.code
                    )
                ),
                onSelected = { interactionsListener.onSelectLanguage(it.tag.code) }
            )
        }

        SettingSection(
            modifier = Modifier,
            title = stringResource(designSystemResource.string.content_restriction),
        ) {
            SettingOptions(
                settingOptionItems = listOf(
                    SettingOptionItem(
                        title = stringResource(designSystemResource.string.strict_restriction),
                        description = stringResource(designSystemResource.string.blurs_all_sensitive_content),
                        tag = ContentRestrictionUiState.RESTRICTED,
                        isSelected = state.selectedContentRestriction == ContentRestrictionUiState.RESTRICTED
                    ),
                    SettingOptionItem(
                        title = stringResource(designSystemResource.string.moderate_restriction),
                        description = stringResource(designSystemResource.string.blurs_explicit_scenes_only),
                        tag = ContentRestrictionUiState.MODERATE_RESTRICTION,
                        isSelected = state.selectedContentRestriction == ContentRestrictionUiState.MODERATE_RESTRICTION
                    ),
                    SettingOptionItem(
                        title = stringResource(designSystemResource.string.unrestricted),
                        description = stringResource(designSystemResource.string.no_content_is_blurred),
                        tag = ContentRestrictionUiState.UNRESTRICTED,
                        isSelected = state.selectedContentRestriction == ContentRestrictionUiState.UNRESTRICTED
                    ),
                ),
                onSelected = { interactionsListener.onSelectContentRestriction(it.tag) }
            )
        }

        MyAccountMediaTab(state, navController)

        NavHost(navController = navController, startDestination = HomeMoviesTapRoute) {
            composable(route = HomeMoviesTapRoute::class) {
                MyAccountMovies(state) { id ->
                    mainTvNavController.navigate(ScreensRoute.MovieDetailsRoute(id))
                }
            }

            composable(route = HomeTvShowsTapRoute::class) {
                MyAccountMovies(state) { id ->
                    mainTvNavController.navigate(ScreensRoute.TvShowDetailsRoute(id))
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
            MediaSection(title = stringResource(designSystemResource.string.my_rating)) {
                items(items = state.myRatingMovies, key = { it.id }) {
                    ImageList(it, onItemClick = { id -> onItemClick(id) })
                }
            }
        }
    }
}


@Composable
fun MyAccountMediaTab(
    state: MyAccountScreenUiState,
    navController: NavHostController,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    var selectedTab by remember {
        mutableIntStateOf(0)
    }
    val tabs = mutableListOf<MediaTabItem>()
    if (state.watchingHistoryMovies.isNotEmpty() && state.myRatingMovies.isNotEmpty()) {
        tabs.add(
            MediaTabItem(
                title = stringResource(designSystemResource.string.movies),
                onFocus = { navController.navigate(HomeMoviesTapRoute) }
            )
        )
    }

    if (state.watchingHistoryTvShows.isNotEmpty() && state.myRatingTvShows.isNotEmpty()) {
        tabs.add(
            MediaTabItem(
                title = stringResource(designSystemResource.string.tv_shows),
                onFocus = { navController.navigate(HomeMoviesTapRoute) }
            )
        )
    }

    if (tabs.isEmpty())
        return

    Box(
        modifier = Modifier
            .focusable(enabled = true, interactionSource)
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
