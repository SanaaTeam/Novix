package com.sanaa.presentation.profileMapper

import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import entity.MediaHistoryItem
import usecase.search.search_param.MediaType

fun MediaHistoryItem.toMediaItemUiModel(): MediaItemUiModel {
    return MediaItemUiModel(
        id = this.id,
        imageUrl = this.posterImageUrl,
        mediaTypeUi = when (this.mediaType) {
            MediaType.MOVIE -> MediaTypeUi.MOVIE
            MediaType.TV_SHOW -> MediaTypeUi.TV_SHOW
        },
    )
}