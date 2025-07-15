package com.sanaa.presentation.model

data class MediaInfoUiModel(
    val title: String,
    val genres: List<String>,
    val rating: String,
    val duration: String,
    val releaseDate: String
)