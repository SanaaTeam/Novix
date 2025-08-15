package com.sanaa.presentation.screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.TopBar
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListBottomSheet
import com.sanaa.presentation.bottomsheets.saveToListBottomsheet.SaveToListBottomSheet
import com.sanaa.presentation.navigation.SearchApiEntryPoint
import com.sanaa.presentation.provider.LocalSaveContentThreshold
import com.sanaa.presentation.provider.LocalThemeProvider
import com.sanaa.presentation.screen.componants.CategoryTabSection
import com.sanaa.presentation.screen.componants.NovixAnimatedSnackBarHost
import com.sanaa.presentation.screen.componants.RequestToLoginBottomSheet
import com.sanaa.presentation.screen.componants.SearchHistoryContent
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SearchScreen(
    navigator: SearchNavigatorApi,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = state.movies.collectAsLazyPagingItems()
    val tvShowsPagingData = state.tvShows.collectAsLazyPagingItems()
    val actorsPagingData = state.actors.collectAsLazyPagingItems()

    EffectHandler(viewModel.effect,navigator)

    CompositionLocalProvider(
        LocalThemeProvider provides state.isDarkMode,
        LocalSaveContentThreshold provides state.safeContentThreshold
    ) {
        SearchScreenContent(
            state = state,
            interactionsListener = viewModel,
            moviesPagingData = moviesPagingData,
            tvShowsPagingData = tvShowsPagingData,
            actorsPagingData = actorsPagingData,
        )
    }
}

@Composable
private fun SearchScreenContent(
    state: SearchScreenUiState,
    interactionsListener: SearchScreenInteractionsListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
) {
    state.movies.let {
        Log.d("test99", "SearchScreenContent: state:${state.movies}")
    }
    NovixScaffold (
        topBar = {
            TopBar(
                modifier = Modifier.statusBarsPadding(), screenTitle = stringResource(R.string.search)
            )
        },
        snackBarHost = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                NovixAnimatedSnackBarHost(
                    data = state.snackBarData,
                    onDismiss = interactionsListener::onSnackBarDismiss
                )
            }

        }
    ){
        Column {
            SearchSection(
                text = state.searchQuery,
                onTextChange = interactionsListener::onSearchQueryChanged,
            )
            AnimatedContent(state.searchQuery.isNotBlank()) {
                when (it) {
                    true -> CategoryTabSection(
                        selectedTabIndex = state.selectedTabIndex,
                        uiState = state,
                        interactionsListener = interactionsListener,
                        modifier = Modifier.align(Alignment.Start),
                        moviesPagingData = moviesPagingData,
                        tvShowsPagingData = tvShowsPagingData,
                        actorsPagingData = actorsPagingData,
                    )

                    false -> SearchHistoryContent(
                        recentSearches = state.recentSearchQueries,
                        recentViewed = state.recentViewedMedia,
                        interactionsListener = interactionsListener,
                    )
                }
            }
            state.selectedMediaToSave?.let { mediaItem ->
                SaveToListBottomSheet(
                    isVisible = state.showSaveToListBottomSheet,
                    mediaId = mediaItem.id.toLong(),
                    interactionsListener = interactionsListener,
                )
            }
            AddBookmarkListBottomSheet(
                isVisible = state.showAddListBottomSheet,
                interactionsListener = interactionsListener,
                mediaId = state.selectedMediaToSave?.id ?: 0
            )
            RequestToLoginBottomSheet(
                interactionsListener = interactionsListener,
                isVisible = state.showLoginBottomSheet,
            )
        }
    }



}



@Composable
fun EffectHandler(effect: Flow<SearchScreenEffects>, navigator: SearchNavigatorApi) {
    val context = LocalContext.current
    val authApi = EntryPointAccessors.fromApplication(
        context,
        SearchApiEntryPoint::class.java
    ).authenticationApi()

    val launcher = launchAuthActivityForResult()


    LaunchedEffect(Unit) {
        effect.collectLatest { effect ->
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
                        StartRoute.TV_SHOW,
                        effect.id
                    )

                SearchScreenEffects.NavigateToLogin -> {
                    launcher.launch(authApi.getLaunchIntent(context))
                }
            }
        }
    }
}
