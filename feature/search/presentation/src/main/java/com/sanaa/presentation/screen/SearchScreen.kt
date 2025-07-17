package com.sanaa.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBar
import com.sanaa.designsystem.design_system.component.nav_bar.NovixNavBarItem
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.component.top_bar.AppTopBar
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.filter_bottomsheet.FilterBottomSheetContent
import com.sanaa.presentation.filter_bottomsheet.FilterBottomSheetInteractionsListener
import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.screen.componants.CategoryTabSection
import com.sanaa.presentation.screen.componants.SearchHistoryContent
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.screen.state.SearchScreenUiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import search.usecase.search_param.MediaFilters

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    filterViewModel: FilterViewModel = koinViewModel<FilterViewModel>(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    val filterUiState by filterViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        filterViewModel.filterResult.collect { filters: MediaFilters? ->
            searchViewModel.onFilterApplied(filters)
        }
    }

    NovixTheme(isSystemInDarkTheme()) {

        SearchScreenContent(
            uiState = uiState,
            filterUiState = filterUiState,
            searchListener = searchViewModel,
            filterListener = filterViewModel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    filterUiState: FilterUiState,
    searchListener: SearchScreenInteractionsListener,
    filterListener: FilterBottomSheetInteractionsListener,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var isSliderDragging by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val dismissSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) showBottomSheet = false
        }
    }

    NovixScaffold(
        topBar = {
            AppTopBar(
                modifier = Modifier.statusBarsPadding(),
                screenTitle = stringResource(R.string.search)
            )
        },
        bottomBar = {
            NovixNavBar(modifier = Modifier.navigationBarsPadding()) {
                val navItems = listOf(
                    R.drawable.icon_home to R.drawable.icon_home_selected,
                    R.drawable.icon_search to R.drawable.icon_search_selected,
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
        }
    ) { _ ->
        Column {
            SearchSection(
                text = uiState.searchQuery,
                onTextChange = searchListener::onSearchQueryChanged,
                onFilterClicked = { showBottomSheet = true }
            )

            AnimatedVisibility(uiState.searchQuery.isNotBlank()) {
                CategoryTabSection(
                    selectedTabIndex = uiState.selectedTabIndex,
                    uiState = uiState,
                    interactionsListener = searchListener,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            AnimatedVisibility(uiState.searchQuery.isBlank()) {
                SearchHistoryContent(
                    recentSearches = uiState.recentSearchQueries,
                    recentViewed = uiState.recentViewedMedia,
                    interactionsListener = searchListener,
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = {
                if (!isSliderDragging) {
                    dismissSheet()
                }
            }, sheetState = sheetState,
            containerColor = Theme.colors.surface
        ) {
            FilterBottomSheetContent(
                uiState = filterUiState,
                listener = filterListener,
                onDismissRequest = dismissSheet,
                isSliderDragging = isSliderDragging,
                onSliderDragStateChanged = { isSliderDragging = it }
            )
        }
    }
}