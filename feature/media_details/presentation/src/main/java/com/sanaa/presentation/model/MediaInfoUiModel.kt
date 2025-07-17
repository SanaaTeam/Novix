package com.sanaa.presentation.model

data class MediaInfoUiModel(
    val title: String,
    val genres: List<String>,
    val rating: String,
    val releaseDate: String,
    val duration: String
)
