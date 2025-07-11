package com.sanaa.presentation.filter_bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import entity.Genre
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import usecase.search.MediaFilters

class FilterViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters>()
    val filterResult = _filterResult.asSharedFlow()

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            val genresFromApi = Genre.entries
            _uiState.update { currentState ->
                currentState.copy(allGenres = genresFromApi, isLoading = false)
            }
        }
    }

    fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange) }
    }

    fun onGenreSelected(genre: Genre) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet()
            if (newSelectedGenres.contains(genre)) {
                newSelectedGenres.remove(genre)
            } else {
                newSelectedGenres.add(genre)
            }
            currentState.copy(selectedGenres = newSelectedGenres)
        }
    }

    fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(imdbRating = newRating) }
    }

    fun onClearFilters() {
        _uiState.update { currentState ->
            FilterUiState(
                allGenres = currentState.allGenres,
                isLoading = false
            )
        }
    }

    fun onApplyClicked() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val mediaFilters = MediaFilters(
                startYear = currentState.yearRange.start.toInt(),
                endYear = currentState.yearRange.endInclusive.toInt(),
                genres = currentState.selectedGenres.toList(),
                imdbRating = currentState.imdbRating.toFloat()
            )
            _filterResult.emit(mediaFilters)
        }
    }
}

