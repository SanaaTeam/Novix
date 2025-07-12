package com.sanaa.presentation.filter_bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usecase.search.MediaFilters

class FilterViewModel : ViewModel(), FilterBottomSheetInteractionsListener {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            _uiState.update { it.copy(allGenres = Genre.entries, isLoading = false) }
        }
    }

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
        viewModelScope.launch {
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