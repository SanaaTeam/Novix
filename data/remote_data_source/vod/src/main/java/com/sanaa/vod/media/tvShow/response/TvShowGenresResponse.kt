package com.sanaa.vod.media.tvShow.response

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TvShowGenresResponse(
    @SerialName("genres") val genres: List<GenreDto>
)
