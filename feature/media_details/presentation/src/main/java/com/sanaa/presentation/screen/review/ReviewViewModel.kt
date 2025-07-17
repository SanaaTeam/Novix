package com.sanaa.presentation.screen.review

import com.sanaa.presentation.details_base.BaseViewModel

class ReviewViewModel(
    private val mediaId: Int,
) : BaseViewModel<ReviewScreenUiState, ReviewScreenEffects>(ReviewScreenUiState()),
    ReviewScreenInteractionListener {
    init {
    }

    override fun onBackClick() {
        emitEffect(ReviewScreenEffects.NavigateBack)
    }
}