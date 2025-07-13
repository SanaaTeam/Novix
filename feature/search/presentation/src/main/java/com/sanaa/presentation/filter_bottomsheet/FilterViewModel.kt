package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
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
        _uiState.update {
            it.copy(
                yearRange = newRange,
                isDefaultState = false,
            )
        }
    }

    override fun onGenreSelected(genre: Genre) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet()
            if (newSelectedGenres.contains(genre)) {
                newSelectedGenres.remove(genre)
            } else {
                newSelectedGenres.add(genre)
            }
            currentState.copy(
                selectedGenres = newSelectedGenres,
                isDefaultState = false
            )
        }
    }

    override fun onRatingChanged(newRating: Int) {
        _uiState.update {
            it.copy(
                imdbRating = newRating,
                isDefaultState = false
            )
        }
    }

    override fun onClearFilters() {
        _uiState.update { currentState ->
            FilterUiState(
                allGenres = currentState.allGenres,
                isLoading = false,
                isDefaultState = true,
            )
        }
    }

    override fun onApplyClicked() {
        tryToExecute(
            callee = {
                val currentState = _uiState.value
                val mediaFilters = MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.toList(),
                    imdbRating = currentState.imdbRating.toFloat()
                )

                if (currentState.isDefaultState)
                    _filterResult.emit(null)
                else
                    _filterResult.emit(mediaFilters)
            }
        )
    }
}

