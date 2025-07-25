package com.sanaa.presentation.filter_bottomsheet

import com.sanaa.presentation.base.BaseViewModel
import com.sanaa.presentation.filter_bottomsheet.state.FilterUiState
import com.sanaa.presentation.filter_bottomsheet.state.GenreUiState
import com.sanaa.presentation.screen.state.mapper.toDomain
import com.sanaa.presentation.screen.state.mapper.toState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.search.search_param.MediaFilters

class FilterViewModel(
    private val manageMovieUseCase: ManageMovieUseCase,
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<FilterUiState, Unit>(
    initialState = FilterUiState(), defaultDispatcher = dispatcher
), FilterBottomSheetInteractionsListener {
    private val _filterResult = MutableSharedFlow<MediaFilters?>()
    val filterResult = _filterResult.asSharedFlow()

    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        tryToExecute(
            callee = {
                val movieGenres = manageMovieUseCase.getMovieGenres()
                val tvShowGenres = manageTvSeriesUseCase.getSeriesGenres()
                val genres = movieGenres.plus(tvShowGenres).toSet()
                //movie or tv series
                updateState {
                    it.copy(allGenres = genres.map { it.toState() })
                }
            })
    }


    override fun onYearRangeChanged(newRange: ClosedFloatingPointRange<Float>) {
        updateState { it.copy(yearRange = newRange) }
    }

    override fun onGenreSelected(genre: GenreUiState) {
        updateState { currentState ->
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
        updateState { it.copy(imdbRating = newRating) }
    }

    override fun onClearFilters() {
        updateState {
            FilterUiState(allGenres = it.allGenres)
        }
    }

    override fun onApplyClicked() {
        tryToExecute(
            callee = {
                val currentState = state.value
                val mediaFilters = MediaFilters(
                    startYear = currentState.yearRange.start.toInt(),
                    endYear = currentState.yearRange.endInclusive.toInt(),
                    genres = currentState.selectedGenres.map { it.toDomain() },
                    imdbRating = currentState.imdbRating.toFloat()
                )
                _filterResult.emit(mediaFilters)
            })
    }
}