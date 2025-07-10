package com.sanaa.search.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchDto(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("poster_path")
    val posterImage: String,
)