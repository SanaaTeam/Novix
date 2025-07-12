package com.sanaa.presentation.screen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
import com.sanaa.presentation.filter_bottomsheet.FilterViewModel
import com.sanaa.presentation.screen.componants.CategoryTabSection
import com.sanaa.presentation.screen.componants.SearchSection
import com.sanaa.presentation.state.SearchScreenUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import usecase.search.MediaFilters

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel<SearchViewModel>(),
    filterViewModel: FilterViewModel = koinViewModel<FilterViewModel>(),
) {
    val uiState by searchViewModel.state.collectAsStateWithLifecycle()
    NovixTheme(isSystemInDarkTheme()) {
        SearchScreenContent(
            uiState = uiState,
            filterViewModel = filterViewModel,
            listener = searchViewModel,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    uiState: SearchScreenUiState,
    filterViewModel: FilterViewModel,
    listener: SearchScreenInteractionsListener,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val filterUiState by filterViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        filterViewModel.filterResult.collectLatest { filters: MediaFilters? ->
            listener.onFilterApplied(filters)

            println("New search filters applied: $filters")
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
            var selectedIndex by remember { mutableIntStateOf(1) }
            NovixNavBar(modifier = Modifier.navigationBarsPadding()) {
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 0,
                    onClick = { selectedIndex = 0 },
                    iconRes = R.drawable.icon_home,
                    selectedIconRes = R.drawable.icon_home_selected,
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 1,
                    onClick = { selectedIndex = 1 },
                    iconRes = R.drawable.icon_search,
                    selectedIconRes = R.drawable.icon_search_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 2,
                    onClick = { selectedIndex = 2 },
                    iconRes = R.drawable.icon_category,
                    selectedIconRes = R.drawable.icon_category_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 3,
                    onClick = { selectedIndex = 3 },
                    iconRes = R.drawable.icon_save,
                    selectedIconRes = R.drawable.icon_save_selected
                )
                NovixNavBarItem(
                    modifier = Modifier.weight(1f),
                    isSelected = selectedIndex == 4,
                    onClick = { selectedIndex = 4 },
                    iconRes = R.drawable.icon_account,
                    selectedIconRes = R.drawable.icon_account_selected
                )
            }
        }
    ) { innerPadding ->
        Column {
            SearchSection(
                text = uiState.searchQuery,
                onTextChange = { listener.onSearchQueryChanged(it) },
                onFilterClicked = { showBottomSheet = true }
            )
            Spacer(Modifier.height(12.dp))

            CategoryTabSection(
                selectedTabIndex = uiState.selectedTabIndex,
                onTabSelected = { listener.onTabSelected(it) },
                uiState = uiState
            )
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Theme.colors.surface
        ) {
            FilterBottomSheetContent(
                uiState = filterUiState,
                onYearRangeChanged = filterViewModel::onYearRangeChanged,
                onGenreSelected = filterViewModel::onGenreSelected,
                onRatingChanged = filterViewModel::onRatingChanged,
                onClearClicked = filterViewModel::onClearFilters,
                onApplyClicked = {
                    filterViewModel.onApplyClicked()
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
                onCloseClicked = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
private fun SearchScreenPreviewLight() {
    NovixTheme(false) {
        //   SearchScreenContent()
    }
}

@Preview(showBackground = true, locale = "en")
@Composable
private fun SearchScreenPreviewDark() {
    NovixTheme(true) {
        //   SearchScreenContent()
    }
}
