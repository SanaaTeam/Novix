package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import usecase.search.MediaFilters

class FilterViewModel(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<FilterUiState>(FilterUiState(), dispatcher),
    FilterBottomSheetInteractionsListener {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange, isDefaultState = false) }
    }

    override fun onGenreSelected(genre: Genre) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet().apply {
                if (contains(genre)) remove(genre) else add(genre)
            }
            currentState.copy(selectedGenres = newSelectedGenres, isDefaultState = false)
        }
    }

    override fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(imdbRating = newRating, isDefaultState = false) }
    }

    override fun onClearFilters() {
        _uiState.update {
            FilterUiState(allGenres = it.allGenres, isLoading = false, isDefaultState = true)
        }
    }

    override fun onApplyClicked() {
        SearchViewModel {
            val currentState = _uiState.value
            val mediaFilters = if (currentState.isDefaultState) {
                null
            } else {
                MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.toList(),
                    imdbRating = if (currentState.imdbRating > 0) currentState.imdbRating.toFloat() else null
                )
            }
            _filterResult.emit(mediaFilters)
        }

    }
}