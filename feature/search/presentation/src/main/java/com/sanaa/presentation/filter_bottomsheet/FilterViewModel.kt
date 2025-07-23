package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import entity.Genre
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.search.search_param.MediaFilters

class FilterViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val manageMovieUseCase: ManageTvSeriesUseCase,
) : BaseViewModel<FilterUiState>(
    initialState = FilterUiState(), defaultDispatcher = dispatcher
), FilterBottomSheetInteractionsListener {
    private val _uiState = MutableStateFlow(FilterUiState())

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                val genres = manageMovieUseCase.getSeriesGenres()
                _uiState.update {
                    it.copy(allGenres = genres.map { genre -> GenreUiState(id = genre.id, name = genre.name
                        )
                    })
                }
            })
    }


    val uiState = _uiState.asStateFlow()

    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        _uiState.update { it.copy(yearRange = newRange) }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        _uiState.update { currentState ->
            val newSelectedGenres = currentState.selectedGenres.toMutableSet().apply {
                if (contains(genre)) {
                    remove(genre)
                } else {
                    add(genre)
                }
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

    override fun onApplyClicked() {
        tryToExecute(
            callee = {
                val currentState = _uiState.value
                val mediaFilters = MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.map {
                        Genre(it.id, it.name)
                    },
                    imdbRating = currentState.imdbRating.toFloat()
                )
                _filterResult.emit(mediaFilters)
            })
    }
}