package com.sanaa.presentation.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.screen.componants.WavyProgressIndicator
import com.sanaa.presentation.screen.state.SearchScreenUiState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import usecase.search.MediaFilters

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
            Spacer(Modifier.height(12.dp))

            if (uiState.isLoading && uiState.searchQuery.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    WavyProgressIndicator()
                }
            } else {
                CategoryTabSection(
                    selectedTabIndex = uiState.selectedTabIndex,
                    onTabSelected = searchListener::onTabSelected,
                    uiState = uiState,
                    listener = searchListener
                )
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = dismissSheet,
            sheetState = sheetState,
            containerColor = Theme.colors.surface
        ) {
            FilterBottomSheetContent(
                uiState = filterUiState,
                listener = filterListener,
                onDismissRequest = dismissSheet
            )
        }
    }
}