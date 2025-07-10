package com.sanaa.presentation.filter_bottomsheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilterViewModel(/* Domains' usecases */) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            delay(500)
            val genresFromApi = listOf("All", "Comedy", "Action", "Crime", "Adventure", "Animation", "Documentary", "Drama", "Family")

            _uiState.update { currentState ->
                currentState.copy(allGenres = genresFromApi, isLoading = false)
            }
        }
    }

    fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange) }
    }

    fun onGenreSelected(genre: String) {
        _uiState.update { it.copy(selectedGenre = genre) }
    }

    fun onRatingChanged(newRating: Int) {
        _uiState.update { it.copy(imdbRating = newRating) }
    }

    fun onClearFilters() {
        _uiState.update { currentState ->
            FilterUiState(
                allGenres = currentState.allGenres,
                isLoading = false,
                selectedGenre = "All"
            )
        }
    }

    fun onApplyClicked() {
        println("Applying filters: ${_uiState.value}")
    }
}
