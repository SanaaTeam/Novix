package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.sanaa.api.StartRoute
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBar
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBarItem
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
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
import org.koin.androidx.compose.koinViewModel
import usecase.search.search_param.MediaFilters

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    onMediaClick: (startRoute: StartRoute, id: Int) -> Unit,
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val moviesPagingData = searchViewModel.moviesPagingData.collectAsLazyPagingItems()
    val tvShowsPagingData = searchViewModel.tvShowsPagingData.collectAsLazyPagingItems()
    val actorsPagingData = searchViewModel.actorsPagingData.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        searchViewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchScreenEffects.NavigateToActorDetails -> onMediaClick(
                    StartRoute.ACTOR, effect.id
                )

                is SearchScreenEffects.NavigateToMovieDetails -> onMediaClick(
                    StartRoute.MOVIE, effect.id
                )

                is SearchScreenEffects.NavigateToTvShowDetails -> onMediaClick(
                    StartRoute.SERIES, effect.id
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

@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    searchListener: SearchScreenInteractionsListener,
    moviesPagingData: LazyPagingItems<MovieUiModel>,
    tvShowsPagingData: LazyPagingItems<TvShowUiModel>,
    actorsPagingData: LazyPagingItems<ActorUiModel>,
    onFilterApplied: (MediaFilters?) -> Unit,
) {

    val dismissSheet: () -> Unit = {
        searchListener.onBottomSheetDragged()
    }


    NovixScaffold(topBar = {
        NovixTopBar(
            modifier = Modifier.statusBarsPadding(), screenTitle = stringResource(R.string.search)
        )
    }, bottomBar = {
        NovixNavBar(modifier = Modifier.navigationBarsPadding()) {
            val navItems = listOf(
                R.drawable.icon_home to R.drawable.icon_home_selected,
                R.drawable.icon_empty_search to R.drawable.icon_search_selected,
                R.drawable.icon_category to R.drawable.icon_category_selected,
                R.drawable.icon_save to R.drawable.icon_save_selected,
                R.drawable.icon_account to R.drawable.icon_account_selected,
            )
            navItems.forEachIndexed { index, (icon, selectedIcon) ->
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = index == 1,
                    onClick = { /* TODO: Handle app-wide navigation */ },
                    iconRes = icon,
                    selectedIconRes = selectedIcon,
                )
            }
        }
    }) { _ ->
        Column {
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
    }

    if (uiState.showBottomSheet) {
        FilterBottomSheet(
            isVisible = uiState.showBottomSheet,
            dismissSheet = dismissSheet,
            filterUiState = filterUiState,
            filterListener = filterListener,
            onFilterApplied = onFilterApplied,
            )
    }
}

