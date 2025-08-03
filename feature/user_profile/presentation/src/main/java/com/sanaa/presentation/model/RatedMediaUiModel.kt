package com.sanaa.presentation.model

data class RatedMediaUiModel(
    val id: Int,
    val posterImageUrl: String?,
    val title: String,
    val rating: Int?,
    val mediaType: String,
)