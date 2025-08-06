package com.sanaa.presentation.model

import com.sanaa.presentation.screen.myRating.MediaTypeUi

data class RatedMediaUiModel(
    val id: Int,
    val posterImageUrl: String?,
    val title: String,
    val rating: Int?,
    val mediaType: MediaTypeUi,
)