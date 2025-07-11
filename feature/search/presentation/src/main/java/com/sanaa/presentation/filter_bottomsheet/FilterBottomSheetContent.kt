package com.sanaa.presentation.filter_bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sanaa.designsystem.R
import com.sanaa.designsystem.design_system.component.button.OutlinedButton
import com.sanaa.designsystem.design_system.component.button.PrimaryButton
import com.sanaa.designsystem.design_system.theme.NovixTheme
import com.sanaa.designsystem.design_system.theme.Theme
import com.sanaa.presentation.filter_bottomsheet.components.BottomSheetHeader
import com.sanaa.presentation.filter_bottomsheet.components.CustomYearRangeSlider
import com.sanaa.presentation.filter_bottomsheet.components.GenreChips
import com.sanaa.presentation.filter_bottomsheet.components.IMDbRatingSelector
import entity.Genre

@Composable
fun FilterBottomSheetContent(
    uiState: FilterUiState,
    onYearRangeChanged: (ClosedFloatingPointRange<Float>) -> Unit,
    onGenreSelected: (Genre) -> Unit,
    onRatingChanged: (Int) -> Unit,
    onClearClicked: () -> Unit,
    onApplyClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(color = Theme.colors.surface)
            .fillMaxWidth()
            .padding(16.dp),
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            BottomSheetHeader(onCancelClicked = onCloseClicked)

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                CustomYearRangeSlider(
                    title = stringResource(R.string.released_year),
                    value = uiState.yearRange,
                    onValueChange = onYearRangeChanged
                )

                GenreChips(
                    genres = uiState.allGenres,
                    selectedGenres = uiState.selectedGenres,
                    onGenreSelected = onGenreSelected
                )

                IMDbRatingSelector(
                    title = stringResource(R.string.imdb_rating),
                    currentRating = uiState.imdbRating,
                    onRatingChanged = onRatingChanged
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        FilterActions(
            onApplyClicked = onApplyClicked,
            onClearClicked = onClearClicked
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
                .height(48.dp), text = stringResource(R.string.apply)
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

@Preview(showBackground = true)
@Composable
fun FilterBottomSheetContentPreview() {
    NovixTheme(true) {
        var previewYearRange by remember { mutableStateOf(1995f..2015f) }
        var previewSelectedGenres by remember {
            mutableStateOf(
                setOf(
                    Genre.ANIMATION,
                    Genre.ACTION
                )
            )
        }
        var previewRating by remember { mutableStateOf(8) }

        val previewState = FilterUiState(
            yearRange = previewYearRange,
            allGenres = Genre.entries.toTypedArray().take(9),
            selectedGenres = previewSelectedGenres,
            imdbRating = previewRating,
            isLoading = false
        )

        FilterBottomSheetContent(
            uiState = previewState,
            onYearRangeChanged = { newRange -> previewYearRange = newRange },
            onGenreSelected = { genre ->
                val updatedGenres = previewSelectedGenres.toMutableSet()
                if (updatedGenres.contains(genre)) updatedGenres.remove(genre)
                else updatedGenres.add(genre)
                previewSelectedGenres = updatedGenres
            },
            onRatingChanged = { newRating -> previewRating = newRating },
            onClearClicked = {
                previewYearRange = 1995f..2012f
                previewSelectedGenres = emptySet()
                previewRating = 8
            },
            onApplyClicked = {},
            onCloseClicked = {}
        )
    }
}
