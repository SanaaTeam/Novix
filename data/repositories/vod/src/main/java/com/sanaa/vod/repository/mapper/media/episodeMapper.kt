package com.sanaa.vod.repository.mapper.media

import com.sanaa.vod.dataSource.remote.dto.tvShow.EpisodeDto
import com.sanaa.vod.util.DateTimeUtils.getLocalDateOrDefault
import entity.Episode

fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = name,
        overview = overview.toString(),
        seasonNumber = seasonNumber,
        number = episodeNumber,
        imdbRating = voteAverage,
        durationMinutes = runtime,
        releaseDate = getLocalDateOrDefault(airDate),
        stillImagePath = getFullImageUrl(stillPath),
        rating = rating?.toInt()
    )
}