package com.sanaa.presentation.model

import com.sanaa.presentation.screen.myRating.MediaTypeUi

data class MediaItemUiModel(
    val id: Int,
    val imageUrl: String?,
    val rating: String? = null,
    val mediaTypeUi: MediaTypeUi,
)