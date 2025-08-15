package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Episode

fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = name,
        overview = overview.orEmpty(),
        seasonNumber = seasonNumber,
        number = episodeNumber,
        imdbRating = voteAverage ?: -1f,
        durationMinutes = runtime ?: -1,
        releaseDate = getLocalDateOrDefault(airDate),
        stillImagePath = getFullImageUrl(stillPath),
        rating = rating?.toInt() ?: -1,
    )
}