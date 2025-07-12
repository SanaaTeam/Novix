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

sealed interface FilterEvent {
    data class YearRangeChanged(val range: ClosedFloatingPointRange<Float>) : FilterEvent
    data class GenreSelected(val genre: Genre) : FilterEvent
    data class RatingChanged(val rating: Int) : FilterEvent
    object Clear : FilterEvent
    object Apply : FilterEvent
    object Close: FilterEvent
}

class FilterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    init {
        fetchGenres()
    }

    fun onEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.YearRangeChanged -> onYearRangeChanged(event.range)
            is FilterEvent.GenreSelected -> onGenreSelected(event.genre)
            is FilterEvent.RatingChanged -> onRatingChanged(event.rating)
            FilterEvent.Clear -> onClearFilters()
            FilterEvent.Apply -> onApplyClicked()
            FilterEvent.Close -> {}
        }
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            _uiState.update { it.copy(allGenres = Genre.entries, isLoading = false) }
        }
    }

    private fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange, isDefaultState = false) }
    }

    private fun onGenreSelected(genre: Genre) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet().apply {
                if (contains(genre)) remove(genre) else add(genre)
            }
            currentState.copy(selectedGenres = newSelectedGenres, isDefaultState = false)
        }
    }

    private fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(imdbRating = newRating, isDefaultState = false) }
    }

    private fun onClearFilters() {
        _uiState.update {
            FilterUiState(allGenres = it.allGenres, isLoading = false, isDefaultState = true)
        }
    }

    private fun onApplyClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val mediaFilters = if (currentState.isDefaultState) {
                null
            } else {
                MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.toList(),
                    imdbRating = if (currentState.imdbRating > 0) currentState.imdbRating.toFloat() else null)
            }
            _filterResult.emit(mediaFilters)
        }
    }
}