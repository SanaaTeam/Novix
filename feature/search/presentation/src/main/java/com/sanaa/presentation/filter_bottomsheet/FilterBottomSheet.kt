package com.sanaa.presentation.filter_bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.base_bottomsheet.BaseBottomSheet
import com.sanaa.designsystem.design_system.component.button.NovixOutlinedButton
import com.sanaa.designsystem.design_system.component.button.NovixPrimaryButton
import com.sanaa.designsystem.design_system.component.indicator.WavyProgressIndicator
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.filter_bottomsheet.components.BottomSheetHeader
import com.sanaa.presentation.filter_bottomsheet.components.CustomYearRangeSlider
import com.sanaa.presentation.filter_bottomsheet.components.GenreChips
import com.sanaa.presentation.filter_bottomsheet.components.IMDbRatingSelector
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import org.koin.androidx.compose.koinViewModel
import usecase.search.search_param.MediaFilters

@Composable
fun FilterBottomSheet(
    dismissSheet: () -> Unit,
    isVisible: Boolean,
    onFilterApplied: (MediaFilters?) -> Unit,
    selectedTabIndex: Int
) {
    var isSliderDragging by remember { mutableStateOf(false) }
    val filterViewModel: FilterViewModel = koinViewModel<FilterViewModel>()
    val filterUiState by filterViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(selectedTabIndex) {
        filterViewModel.fetchGenresByTab(selectedTabIndex)
    }

    LaunchedEffect(Unit) {
        filterViewModel.filterResult.collect { filters ->
            onFilterApplied(filters)
        }
    }

    BaseBottomSheet(
        isVisible = isVisible,
        onDismiss = {
            if (!isSliderDragging) {
                dismissSheet()
            }
        },
        modifier = Modifier.fillMaxHeight(0.85f)

    ) {
        FilterBottomSheetContent(
            onDismissRequest = dismissSheet,
            uiState = filterUiState,
            listener = filterViewModel,
            isSliderDragging = isSliderDragging,
            onSliderDragStateChanged = { isSliderDragging = it }
        )

    }
}


@Composable
fun FilterBottomSheetContent(
    uiState: FilterUiState,
    listener: FilterBottomSheetInteractionsListener,
    onDismissRequest: () -> Unit,
    isSliderDragging: Boolean,
    onSliderDragStateChanged: (isDragging: Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(color = Theme.colors.surface)
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f, fill = false)
                .verticalScroll(
                    state = rememberScrollState(),
                    enabled = !isSliderDragging
                ),
        ) {
            BottomSheetHeader(onCancelClicked = onDismissRequest)

            AnimatedVisibility(visible = uiState.isLoading) {
                WavyProgressIndicator()
            }

            AnimatedVisibility(visible = !uiState.isLoading) {
                Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
                    CustomYearRangeSlider(
                        title = stringResource(R.string.released_year),
                        value = uiState.yearRange,
                        onValueChange = listener::onYearRangeChanged,
                        onDragStateChanged = onSliderDragStateChanged
                    )
                    GenreChips(
                        genres = uiState.allGenres,
                        selectedGenres = uiState.selectedGenres,
                        onGenreSelected = listener::onGenreSelected,
                        animateWidth = false,
                    )
                    IMDbRatingSelector(
                        title = stringResource(R.string.imdb_rating),
                        currentRating = uiState.imdbRating,
                        onRatingChanged = listener::onRatingChanged
                    )
                }
            }
        }
        FilterActions(
            isApplyEnabled = uiState.hasUserSelectedFilters,
            onApplyClicked = {
                listener.onApplyClicked()
                onDismissRequest()
            },
            onClearClicked = listener::onClearFilters
        )
    }
}


@Composable
private fun FilterActions(
    isApplyEnabled: Boolean,
    onApplyClicked: () -> Unit,
    onClearClicked: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NovixPrimaryButton(
            onClick = onApplyClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.apply),
            isEnabled = isApplyEnabled
        )
        NovixOutlinedButton(
            onClick = onClearClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.clear)
        )
    }
}