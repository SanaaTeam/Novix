//package com.sanaa.presentation.filter_bottomsheet
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.sanaa.designsystem.R
//import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
//import com.sanaa.designsystem.design_system.component.button.NovixOutlinedButton
//import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
//import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator
//import com.sanaa.designsystem.design_system.theme.Theme
//import com.sanaa.presentation.filter_bottomsheet.FilterViewModel.Companion.MOVIE_INDEX
//import com.sanaa.presentation.filter_bottomsheet.components.BottomSheetHeader
//import com.sanaa.presentation.filter_bottomsheet.components.CustomYearRangeSlider
//import com.sanaa.presentation.filter_bottomsheet.components.GenreChips
//import com.sanaa.presentation.filter_bottomsheet.components.IMDbRatingSelector
//import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
//import usecase.search.search_param.MediaFilters
//
//@Composable
//fun FilterBottomSheet(
//    dismissSheet: () -> Unit,
//    isVisible: Boolean,
//    onFilterApplied: (Int,MediaFilters?) -> Unit,
//    selectedTabIndex: Int
//) {
//    var isSliderDragging by remember { mutableStateOf(false) }
//    val filterViewModel: FilterViewModel = hiltViewModel()
//    val filterUiState by filterViewModel.state.collectAsStateWithLifecycle()
//
//    LaunchedEffect(Unit) {
//        filterViewModel.filterResult.collect { (tabIndex, filters) ->
//            if (tabIndex == selectedTabIndex) {
//                onFilterApplied(tabIndex,filters)
//            }
//        }
//    }
//
//    BaseBottomSheet(
//        isVisible = isVisible,
//        onDismiss = {
//            if (!isSliderDragging) {
//                dismissSheet()
//            }
//        },
//
//        ) {
//        FilterBottomSheetContent(
//            onDismissRequest = dismissSheet,
//            uiState = filterUiState,
//            listener = filterViewModel,
//            selectedTabIndex = selectedTabIndex,
//            isSliderDragging = isSliderDragging,
//            onSliderDragStateChanged = { isSliderDragging = it }
//        )
//
//    }
//}
//
//
//@Composable
//fun FilterBottomSheetContent(
//    uiState: FilterUiState,
//    selectedTabIndex: Int,
//    listener: FilterBottomSheetInteractionsListener,
//    onDismissRequest: () -> Unit,
//    isSliderDragging: Boolean,
//    onSliderDragStateChanged: (isDragging: Boolean) -> Unit,
//) {
//    val displayedGenres = if (selectedTabIndex == MOVIE_INDEX) {
//        uiState.movieGenres
//    } else {
//        uiState.tvGenres
//    }
//
//    val filters = if (selectedTabIndex == MOVIE_INDEX) {
//        uiState.movieFilters
//    } else {
//        uiState.tvFilters
//    }
//
//    Column(
//        modifier = Modifier
//            .background(color = Theme.colors.surface)
//            .fillMaxWidth()
//            .padding(16.dp),
//    ) {
//        Column(
//            modifier = Modifier
//                .weight(1f, fill = false)
//                .verticalScroll(
//                    state = rememberScrollState(),
//                    enabled = !isSliderDragging
//                ),
//        ) {
//            BottomSheetHeader(onCancelClicked = onDismissRequest)
//
//            AnimatedVisibility(visible = uiState.isLoading) {
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    WavyProgressIndicator()
//                }
//
//            }
//            AnimatedVisibility(visible = !uiState.isLoading) {
//                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
//                    CustomYearRangeSlider(
//                        title = stringResource(R.string.released_year),
//                        value = filters.yearRange,
//                        onValueChange = { listener.onYearRangeChanged(selectedTabIndex, it) },
//                        onDragStateChanged = onSliderDragStateChanged
//                    )
//                    GenreChips(
//                        genres = displayedGenres,
//                        selectedGenres = filters.selectedGenres,
//                        onGenreSelected = { listener.onGenreSelected(selectedTabIndex, it) },
//                        animateWidth = true,
//                    )
//                    IMDbRatingSelector(
//                        title = stringResource(R.string.imdb_rating),
//                        currentRating = filters.imdbRating,
//                        onRatingChanged = { listener.onRatingChanged(selectedTabIndex, it) },
//                        modifier = Modifier.padding(top = 20.dp)
//                    )
//                }
//            }
//        }
//        FilterActions(
//            isApplyEnabled = filters.hasUserSelectedFilters,
//            onApplyClicked = {
//                listener.onApplyClicked(selectedTabIndex)
//                onDismissRequest()
//            },
//            onClearClicked = { listener.onClearFilters(selectedTabIndex) }
//        )
//    }
//}
//
//
//@Composable
//private fun FilterActions(
//    isApplyEnabled: Boolean,
//    onApplyClicked: () -> Unit,
//    onClearClicked: () -> Unit,
//) {
//    Column(
//        modifier = Modifier.padding(top = 24.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        NovixPrimaryButton(
//            onClick = onApplyClicked,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            text = stringResource(R.string.apply),
//            isEnabled = isApplyEnabled
//        )
//        NovixOutlinedButton(
//            onClick = onClearClicked,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp),
//            text = stringResource(R.string.clear)
//        )
//    }
//}
