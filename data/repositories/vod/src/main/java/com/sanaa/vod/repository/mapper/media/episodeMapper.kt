package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import entity.Episode
import kotlinx.datetime.LocalDate

fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = name,
        overview = overview.toString(),
        seasonNumber = seasonNumber,
        number = episodeNumber,
        imdbRating = voteAverage,
        durationMinutes = runtime,
        releaseDate = airDate?.let { LocalDate.parse(it) } ?: LocalDate.parse("1970-01-01"),
        stillImagePath = getFullImageUrl(stillPath),
        rating = rating?.toInt()
    )
}