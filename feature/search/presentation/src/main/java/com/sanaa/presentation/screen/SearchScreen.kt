package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.SearchNavigatorApi
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.top_bar.NovixTopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.presentation.filter_bottomsheet.FilterBottomSheet
import com.sanaa.presentation.screen.componants.CategoryTabSection
import com.sanaa.presentation.screen.componants.SearchHistoryContent
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.screen.state.ActorUiModel
import com.sanaa.presentation.screen.state.MovieUiModel
import com.sanaa.presentation.screen.state.SearchScreenEffects
import com.sanaa.presentation.screen.state.SearchScreenUiState
import com.sanaa.presentation.screen.state.TvShowUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import usecase.search.search_param.MediaFilters

@Composable
fun SearchScreen(
    navigator: SearchNavigatorApi,
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = searchViewModel.moviesPagingData.collectAsLazyPagingItems()
    val tvShowsPagingData = searchViewModel.tvShowsPagingData.collectAsLazyPagingItems()
    val actorsPagingData = searchViewModel.actorsPagingData.collectAsLazyPagingItems()

    val context = LocalContext.current
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
            onFilterApplied = searchViewModel::onFilterApplied
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    searchListener: SearchScreenInteractionsListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
    onFilterApplied: (MediaFilters?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val dismissSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) searchListener.onBottomSheetDragged()
        }
    }

    Column {
        NovixTopBar(
            modifier = Modifier.statusBarsPadding(), screenTitle = stringResource(R.string.search)
        )

        SearchSection(
            text = uiState.searchQuery,
            onTextChange = searchListener::onSearchQueryChanged,
            onFilterClicked = { searchListener.onFilterClicked() },
            isFilterButtonVisible = uiState.isFilterVisible(),
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

    if (uiState.showBottomSheet) {
        FilterBottomSheet(
            dismissSheet = dismissSheet,
            sheetState = sheetState,
            onFilterApplied = onFilterApplied,
        )
    }
}