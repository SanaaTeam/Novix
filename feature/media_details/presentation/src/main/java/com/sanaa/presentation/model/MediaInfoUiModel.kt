package com.sanaa.presentation.model

sealed class MediaInfoUiModel {
    abstract val title: String
    abstract val genres: List<String>
    abstract val rating: String
    abstract val releaseDate: String

    data class Movie(
        override val title: String,
        override val genres: List<String>,
        override val rating: String,
        override val releaseDate: String,
        val duration: String
    ) : MediaInfoUiModel()

    data class Series(
        override val title: String,
        override val genres: List<String>,
        override val rating: String,
        override val releaseDate: String,
        val seasonCount: Int
    ) : MediaInfoUiModel()
}
