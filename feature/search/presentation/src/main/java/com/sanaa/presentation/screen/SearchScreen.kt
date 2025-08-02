package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.StartRoute
import com.sanaa.api.launchAuthActivityForResult
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.navigation.SearchApiEntryPoint
import com.sanaa.presentation.screen.componants.CategoryTabSection
import com.sanaa.presentation.screen.componants.RequestToLoginBottomSheet
import com.sanaa.presentation.screen.componants.SearchHistoryContent
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navigator: SearchNavigatorApi,
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = uiState.movies.collectAsLazyPagingItems()
    val tvShowsPagingData = uiState.tvShows.collectAsLazyPagingItems()
    val actorsPagingData = uiState.actors.collectAsLazyPagingItems()

    val context = LocalContext.current
    val authApi = EntryPointAccessors.fromApplication(
        context,
        SearchApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult(
        loggedInWithSessionId = {
            searchViewModel.updateUserStatus()
        },
        loggedInAsGuest = {
            searchViewModel.updateUserStatus()
        }
    )




    LaunchedEffect(Unit) {
        searchViewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchScreenEffects.NavigateToActorDetails ->
                    navigator.navigateToMediaDetails(
                        context,
                        StartRoute.ACTOR,
                        effect.id
                    )

                is SearchScreenEffects.NavigateToMovieDetails ->
                    navigator.navigateToMediaDetails(
                        context,
                        StartRoute.MOVIE,
                        effect.id
                    )

                is SearchScreenEffects.NavigateToTvShowDetails ->
                    navigator.navigateToMediaDetails(
                        context,
                        StartRoute.SERIES,
                        effect.id
                    )

                SearchScreenEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }

    NovixTheme(isSystemInDarkTheme()) {
        SearchScreenContent(
            uiState = uiState,
            searchListener = searchViewModel,
            moviesPagingData = moviesPagingData,
            tvShowsPagingData = tvShowsPagingData,
            actorsPagingData = actorsPagingData,
        )
    }
}

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    searchListener: SearchScreenInteractionsListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {
    Column {
        NovixTopBar(
            modifier = Modifier.statusBarsPadding(), screenTitle = stringResource(R.string.search)
        )

        SearchSection(
            text = uiState.searchQuery,
            onTextChange = searchListener::onSearchQueryChanged,
        )

        AnimatedContent(uiState.searchQuery.isNotBlank()) {
            when (it) {
                true -> CategoryTabSection(
                    selectedTabIndex = uiState.selectedTabIndex,
                    uiState = uiState,
                    interactionsListener = searchListener,
                    modifier = Modifier.align(Alignment.Start),
                    moviesPagingData = moviesPagingData,
                    tvShowsPagingData = tvShowsPagingData,
                    actorsPagingData = actorsPagingData,
                )

                false -> SearchHistoryContent(
                    recentSearches = uiState.recentSearchQueries,
                    recentViewed = uiState.recentViewedMedia,
                    interactionsListener = searchListener,
                )
            }
        }
    }


    RequestToLoginBottomSheet(
        onDismiss = {searchListener.onBottomSheetDismiss()},
        onLoginButtonClick = { searchListener.onLoginButtonClick() },
        isVisible = uiState.showLoginBottomSheet,
    )
}