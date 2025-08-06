package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.sanaa.presentation.model.toUiModel
import com.sanaa.presentation.savedBase.BasePagingSource
import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import usecase.custom_list.ManageSavedListItemsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseViewModel<SavedDetailsScreenUiState, PlaylistDetailsScreenEffect>(SavedDetailsScreenUiState()),
    PlaylistDetailsInteractionListener {
    private val listId: Int = checkNotNull(savedStateHandle["listId"]) {
        "listId is required in SavedStateHandle"
    }

    private val title: String = checkNotNull(savedStateHandle["title"]) {
        "title is required in SavedStateHandle"
    }

    init {
        loadItemsInSaved(listId)
    }


    private fun loadItemsInSaved(categoryId: Int) {
        tryToCollect(

            callee = {
                loadSavedMovies(categoryId)
            },
            onCollect = { movies ->
                updateState {
                    it.copy(movieList = flowOf(movies), title = title, isLoading = false)
                }
            },
            onError = { exception ->
                if (exception is NoNetworkException) {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                } else {
                    updateState {
                        it.copy(errorMessage = exception.message, isLoading = false)
                    }
                }
            }
        )
    }

    override fun onMediaClick(
        mediaId: Int,
    ) {
        //  TODO("Not yet implemented")
    }

    override fun onSaveIconClick(mediaItem: MediaItem) {
        //   TODO("Not yet implemented")
    }

    override fun onBackClick() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBack)
    }

    private fun loadSavedMovies(listId: Int): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createSavedMoviesPagingSource(listId) },
            mapper = Movie::toUiModel
        )
    }

    private fun createSavedMoviesPagingSource(listId: Int): PagingSource<Int, Movie> {
        return BasePagingSource { page ->
            manageSavedListItemsUseCase.getAllItemsInSavedList(listId, page)
        }
    }

}
