package com.sanaa.series.mapper

import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.VideoDto
import entity.Episode
import entity.Genre
import entity.Season
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun TvSeriesDto.toEntity(video: List<VideoDto>): TvSeries {
    return TvSeries(
        id = id,
        title = name,
        overview = overview,
        posterImageUrl = posterPath,
        imdbRating = voteAverage,
        releaseDate = LocalDate.parse(firstAirDate),
        genres = genres.map { it.toEntity() },
        seasonsCount = seasonsCount,
        trailerUrl = video.map { it.toEntity() }.first(),
    )
}

fun SeasonDto.toEntity(): Season {
    return Season(
        id = id,
        title = name,
        overview = overview,
        number = seasonNumber,
        episodes = episodes.map { it.toEntity() }
    )
}


fun EpisodeDto.toEntity(): Episode {
    return Episode(
        id = id,
        title = name,
        overview = overview,
        seasonNumber = seasonNumber,
        number = episodeNumber,
        imdbRating = voteAverage,
        durationMinutes = runtime,
        releaseDate = LocalDate.parse(airDate),
    )
}


fun VideoDto.toEntity(): String {
    return when (site) {
        "YouTube" -> "https://www.youtube.com/watch?v=$key"
        else -> ""
    }
}

fun GenreDto.toEntity(): Genre {
    return when (id) {
        10759 -> Genre.WAR_AND_POLITICS
        16 -> Genre.ANIMATION
        35 -> Genre.COMEDY
        80 -> Genre.CRIME
        99 -> Genre.DOCUMENTARY
        18 -> Genre.DRAMA
        10751 -> Genre.FAMILY
        12 -> Genre.ADVENTURE
        else -> Genre.SCIENCE_FICTION
    }
}