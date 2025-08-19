package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.filter
import com.sanaa.presentation.model.toUiModel
import com.sanaa.presentation.savedBase.BasePagingSource
import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.playlist.SnackData
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import entity.Movie
import exceptions.NoNetworkException
import exceptions.NovixAppException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import repository.ContentRestriction
import service.VodStringProvider
import usecase.MangeUserPreferenceUseCase
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase,
    private val manageSavedListsUseCase: ManageSavedListsUseCase,
    private val manageUserPreferenceUseCase: MangeUserPreferenceUseCase,
    private val stringProvider: VodStringProvider,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BaseViewModel<SavedDetailsScreenUiState, PlaylistDetailsScreenEffect>(
    SavedDetailsScreenUiState(), dispatcher
),
    PlaylistDetailsInteractionListener {
    private val listId: Int = checkNotNull(savedStateHandle["listId"]) {
        "listId is required in SavedStateHandle"
    }
    private val listTitle: String = checkNotNull(savedStateHandle["title"]) {
        "title is required in SavedStateHandle"
    }

    init {
        updateState { copy(listId = listId, title = listTitle) }
        getUserContentRestrictionPreference()
        loadItemsInSavedList(listId)
    }


    private fun getUserContentRestrictionPreference() {
        tryToCollect(
            block = { manageUserPreferenceUseCase.getContentRestriction() },
            onCollect = ::onContentRestrictionCollect,
            onError = ::onDataLoadError
        )
    }

    private fun onContentRestrictionCollect(contentRestriction: ContentRestriction) {
        val threshold = when (contentRestriction) {
            ContentRestriction.RESTRICTED -> STRICT_CONTENT_THRESHOLD
            ContentRestriction.MODERATE_RESTRICTION -> MODERATE_CONTENT_THRESHOLD
            ContentRestriction.UNRESTRICTED -> UNRESTRICTED_CONTENT_THRESHOLD
        }
        updateState {
            copy(safeContentThreshold = threshold)
        }
    }

    private fun loadItemsInSavedList(listId: Int) {
        tryToCollect(
            onStart = { updateState { copy(isLoading = true) } },
            block = { loadSavedMovies(listId) },
            onCollect = { updateState { copy(movieList = flowOf(it), isLoading = false) } },
            onError = ::onDataLoadError
        )
    }

    override fun onMediaClick(mediaId: Int, mediaType: MediaTypeUi) {
        emitEffect(PlaylistDetailsScreenEffect.NavigateToMediaDetails(mediaId, mediaType))
    }

    override fun onDeleteIconClick(mediaItem: MediaItem) {
        updateState {
            copy(
                showRemoveFromListBottomSheet = true,
                selectedMediaToRemove = mediaItem
            )
        }
    }

    override fun onDismissRemoveFromListBottomSheet() {
        updateState { copy(showRemoveFromListBottomSheet = false, selectedMediaToRemove = null) }
    }

    override fun onDismissListBottomSheetAfterRemoveSuccess(deselectedListIds: List<Int>) {
        if (listId in deselectedListIds) {
            updateState {
                copy(
                    movieList = state.value.movieList.map { pagingData ->
                        pagingData.filter { mediaItem ->
                            mediaItem.id != state.value.selectedMediaToRemove?.id
                        }
                    }
                )
            }
        }
        updateState {
            copy(
                showRemoveFromListBottomSheet = false,
                selectedMediaToRemove = null,
                snackBarData = SnackData(
                    message = stringProvider.deleteFromListSuccess,
                    isError = false
                )
            )
        }
    }

    override fun onBackClick() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBack)
    }

    override fun onDeleteListClick() {
        updateState { copy(showListDeletionConfirmationBottomSheet = true) }
    }

    override fun onDeleteListConfirmed() {
        tryToExecute(
            onStart = { updateState { copy(isLoading = true) } },
            block = { manageSavedListsUseCase.deleteSavedList(listId) },
            onSuccess = ::onListDeletionSuccess,
            onError = ::onListDeletionFailed,
        )
    }

    private fun onListDeletionSuccess(unit: Unit) {
        updateState {
            copy(
                isLoading = false,
            )
        }
        emitEffect(PlaylistDetailsScreenEffect.NavigateBackAfterDelete)
    }

    private fun onListDeletionFailed(e: NovixAppException) {
        updateState {
            when (e) {
                is NoNetworkException -> copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = false
                    )
                )

                else -> copy(
                    isLoading = false,
                    noInternetConnection = false,
                    snackBarData = SnackData(
                        message = stringProvider.deleteListFailed,
                        isError = true
                    )
                )
            }
        }
    }

    override fun onDismissConfirmationBottomSheet() {
        updateState { copy(showListDeletionConfirmationBottomSheet = false) }
    }

    override fun onListDeletedSuccessfully() {
        emitEffect(PlaylistDetailsScreenEffect.NavigateBackAfterDelete)
    }

    override fun onSnackBarDismiss() {
        updateState { copy(snackBarData = null) }
    }

    override fun onRetryClick() {
        loadItemsInSavedList(listId)
    }

    fun loadSavedMovies(listId: Int): Flow<PagingData<MediaItem>> {
        return createPagingFlow(
            pagingSourceFactory = { createSavedMoviesPagingSource(listId) },
            mapper = Movie::toUiModel
        )
    }

    private fun createSavedMoviesPagingSource(listId: Int): PagingSource<Int, Movie> {
        return BasePagingSource(onError = ::onDataLoadError) { page ->
            manageSavedListItemsUseCase.getItemsInSavedList(listId, page)
        }
    }

    private fun onDataLoadError(e: NovixAppException) {
        updateState {
            when (e) {
                is NoNetworkException -> copy(
                    isLoading = false,
                    noInternetConnection = true,
                    snackBarData = SnackData(
                        message = stringProvider.noInternetConnectionError,
                        isError = false
                    )
                )

                else -> copy(
                    isLoading = false,
                    noInternetConnection = false,
                    snackBarData = SnackData(
                        message = stringProvider.somethingWentWrongError,
                        isError = true
                    )
                )
            }
        }
    }

    private companion object {
        const val STRICT_CONTENT_THRESHOLD = 0.9f
        const val MODERATE_CONTENT_THRESHOLD = 0.5f
        const val UNRESTRICTED_CONTENT_THRESHOLD = 0.0f
    }
}
