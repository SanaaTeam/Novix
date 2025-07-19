package com.sanaa.presentation.filter_bottomsheet

import com.example.preferences.service.GenreLocalizer
import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import entity.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import search.usecase.search_param.MediaFilters

class FilterViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val genreLocalizer: GenreLocalizer,
) : BaseViewModel<FilterUiState>(
    initialState = FilterUiState(),
    defaultDispatcher = dispatcher
),
    FilterBottomSheetInteractionsListener {


    private val _uiState = MutableStateFlow(FilterUiState(allGenres = Genre.entries.map {
        genreLocalizer.getLocalizedName(it.name)
    }))
    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange, isDefaultState = false) }
    }

    override fun onGenreSelected(genre: String) {
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
        tryToExecute(
            callee = {
                val currentState = _uiState.value
                val mediaFilters = if (currentState.isDefaultState) {
                    null
                } else {
                    MediaFilters(
                        startYear = currentState.yearRange.start.toInt(),
                        endYear = currentState.yearRange.endInclusive.toInt(),
                        genres = currentState.selectedGenres.toList().mapNotNull { genreName ->
                            Genre.entries.find {
                                it.name.equals(
                                    genreName,
                                    ignoreCase = true
                                )
                            }
                        },
                        imdbRating = currentState.imdbRating.toFloat()
                    )
                }
                _filterResult.emit(mediaFilters)

            }
        )
    }
}