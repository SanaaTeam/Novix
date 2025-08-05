package com.sanaa.presentation.screen.savedDetails

import com.sanaa.presentation.savedBase.BaseViewModel
import com.sanaa.presentation.screen.savedDetails.state.MediaItem
import com.sanaa.presentation.screen.savedDetails.state.SavedDetailsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import usecase.CheckIfUserIsLoggedInUseCase
import javax.inject.Inject

@HiltViewModel
class SavedDetailsScreenViewModel @Inject constructor(
    private val checkUserLogin: CheckIfUserIsLoggedInUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    BaseViewModel<SavedDetailsScreenUiState, SavedDetailsScreenEffect>(SavedDetailsScreenUiState()),
    SavedDetailsInteractionListener {
    override fun onMediaClick(
        mediaId: Int,
    ) {
        TODO("Not yet implemented")
    }

    override fun onSaveIconClick(mediaItem: MediaItem) {
        TODO("Not yet implemented")
    }


}
