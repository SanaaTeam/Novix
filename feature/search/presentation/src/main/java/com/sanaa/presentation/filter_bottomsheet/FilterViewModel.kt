package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.preferences.service.GenreLocalizer
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FilterViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val genreLocalizer: GenreLocalizer,
) : BaseViewModel<FilterUiState>(
    initialState = FilterUiState(),
    defaultDispatcher = dispatcher
), FilterBottomSheetInteractionsListener {
    private val _uiState = MutableStateFlow(FilterUiState(allGenres = Genre.entries.map {
        genreLocalizer.getLocalizedName(it.name)
    }))
    val uiState = _uiState.asStateFlow()

    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange) }
    }

    override fun onGenreSelected(genre: String) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet().apply {
                if (contains(genre)) remove(genre) else add(genre)
            }
            currentState.copy(selectedGenres = newSelectedGenres)
        }
    }

    override fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(imdbRating = newRating) }
    }

    override fun onClearFilters() {
        _uiState.update {
            FilterUiState(allGenres = it.allGenres)
        }
    }
}