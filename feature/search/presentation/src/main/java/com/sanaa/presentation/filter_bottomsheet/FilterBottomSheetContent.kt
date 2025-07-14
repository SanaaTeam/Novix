package com.sanaa.presentation.filter_bottomsheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.filter_bottomsheet.components.BottomSheetHeader
import com.sanaa.presentation.filter_bottomsheet.components.CustomYearRangeSlider
import com.sanaa.presentation.filter_bottomsheet.components.GenreChips
import com.sanaa.presentation.filter_bottomsheet.components.IMDbRatingSelector
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.screen.componants.WavyProgressIndicator

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
                        onGenreSelected = listener::onGenreSelected
                    )
                    IMDbRatingSelector(
                        title = stringResource(R.string.imdb_rating),
                        currentRating = uiState.imdbRating,
                        onRatingChanged = listener::onRatingChanged
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        FilterActions(
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
    onApplyClicked: () -> Unit,
    onClearClicked: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PrimaryButton(
            onClick = onApplyClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.apply)
        )
        OutlinedButton(
            onClick = onClearClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(R.string.clear)
        )
    }
}