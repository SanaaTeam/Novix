package com.sanaa.vod.dataSource.remote.dto.tvShow


import com.sanaa.vod.dataSource.remote.dto.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvShowDto(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("overview")
    val overview: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Float = 0f,
    @SerialName("number_of_seasons")
    val seasonsCount: Int = 0,
    @SerialName("genres")
    val genres: List<GenreDto> = emptyList(),
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("genre_ids")
    val genresId: List<Int>? = null,
    @SerialName("character")
    val character: String? = null,
    @SerialName("credit_id")
    val creditId: String? = null,
    @SerialName("episode_count")
    val episodeCount: Int = 0,
    @SerialName("rating")
    val rating: Float? = null
)
