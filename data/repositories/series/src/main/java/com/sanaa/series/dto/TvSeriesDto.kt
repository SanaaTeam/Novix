package com.sanaa.series.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TvSeriesDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("overview") val overview: String = "",
    @SerialName("poster_path") val posterPath: String = "",
    @SerialName("vote_average") val voteAverage: Float = 0f,
    @SerialName("number_of_seasons") val seasonsCount: Int = 0,
    @SerialName("genres") val genres: List<GenreDto> = emptyList(),
    @SerialName("first_air_date") val firstAirDate: String = ""
)

@Serializable
data class EpisodeDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("overview") val overview: String = "",
    @SerialName("season_number") val seasonNumber: Int = 0,
    @SerialName("episode_number") val episodeNumber: Int = 0,
    @SerialName("vote_average") val voteAverage: Float = 0f,
    @SerialName("air_date") val airDate: String = "",
    @SerialName("runtime") val runtime: Int = 0,
)

@Serializable
data class SeasonDto(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "",
    @SerialName("overview") val overview: String = "",
    @SerialName("season_number") val seasonNumber: Int = 0,
    @SerialName("episodes") val episodes: List<EpisodeDto> = emptyList(),
)


@Serializable
data class TvSeriesVideoDto(
    @SerialName("id") val id: String = "",
    @SerialName("key") val key: String = "",
    @SerialName("name") val name: String = "",
    @SerialName("site") val site: String = "",
    @SerialName("type") val type: String = ""
)

@Serializable
data class GenreDto(
    @SerialName("id") val id: Int = 0, @SerialName("name") val name: String = ""
)

@Serializable
class TvSeriesImageDto(
    @SerialName("file_path") val filePath: String,
)