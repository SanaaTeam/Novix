package com.sanaa.series.mapper

import com.sanaa.series.dto.EpisodeDto
import com.sanaa.series.dto.GenreDto
import com.sanaa.series.dto.ImageDto
import com.sanaa.series.dto.SeasonDto
import com.sanaa.series.dto.TvSeriesDto
import com.sanaa.series.dto.VideoDto
import entity.Episode
import entity.Genre
import entity.Season
import entity.TvSeries
import kotlinx.datetime.LocalDate

fun TvSeriesDto.toEntity(): TvSeries {
    return TvSeries(
        id = id,
        title = name,
        overview = overview,
        posterImageUrl = buildPosterUrl(posterPath),
        imdbRating = voteAverage,
        releaseDate = LocalDate.parse(firstAirDate),
        genres = genres.map { it.toEntity() },
        seasonsCount = seasonsCount,
    )
}

fun SeasonDto.toEntity(): Season {
    return Season(
        id = id,
        title = name,
        overview = overview,
        number = seasonNumber,
        episodes = episodes.map { it.toEntity() })
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


fun GenreDto.toEntity(): Genre {
    return when (id) {
        28 -> Genre.ACTION
        12 -> Genre.ADVENTURE
        16 -> Genre.ANIMATION
        35 -> Genre.COMEDY
        80 -> Genre.CRIME
        99 -> Genre.DOCUMENTARY
        18 -> Genre.DRAMA
        10751 -> Genre.FAMILY
        14 -> Genre.FANTASY
        36 -> Genre.HISTORY
        27 -> Genre.HORROR
        10402 -> Genre.MUSIC
        9648 -> Genre.MYSTERY
        10763 -> Genre.NEWS
        10764 -> Genre.REALITY
        10749 -> Genre.ROMANCE
        878 -> Genre.SCIENCE_FICTION
        10765 -> Genre.SCI_FI_AND_FANTASY
        10766 -> Genre.SOAP
        10762 -> Genre.KIDS
        10767 -> Genre.TALK
        53 -> Genre.THRILLER
        10768 -> Genre.WAR_AND_POLITICS
        10752 -> Genre.WAR
        10770 -> Genre.TV_MOVIE
        37 -> Genre.WESTERN
        10759 -> Genre.ACTION_AND_ADVENTURE
        else -> Genre.DRAMA
    }
}

fun Genre.toDtoId(): Int {
    return when (this) {
        Genre.ACTION -> 28
        Genre.ADVENTURE -> 12
        Genre.COMEDY -> 35
        Genre.DRAMA -> 18
        Genre.HORROR -> 27
        Genre.SCIENCE_FICTION -> 878
        Genre.FANTASY -> 14
        Genre.ROMANCE -> 10749
        Genre.THRILLER -> 53
        Genre.DOCUMENTARY -> 99
        Genre.ANIMATION -> 16
        Genre.CRIME -> 80
        Genre.FAMILY -> 10751
        Genre.HISTORY -> 36
        Genre.KIDS -> 10762
        Genre.MYSTERY -> 9648
        Genre.MUSIC -> 10402
        Genre.NEWS -> 10763
        Genre.REALITY -> 10764
        Genre.SOAP -> 10766
        Genre.TALK -> 10767
        Genre.WAR -> 10752
        Genre.WAR_AND_POLITICS -> 10768
        Genre.WESTERN -> 37
        Genre.TV_MOVIE -> 10770
        Genre.ACTION_AND_ADVENTURE -> 10759
        Genre.SCI_FI_AND_FANTASY -> 10765
    }
}


fun buildPosterUrl(posterPath: String?): String {
    return "https://image.tmdb.org/t/p/w500$posterPath"
}

fun VideoDto.toEntity(): String {
    return when (type) {
        "YouTube" -> "https://www.youtube.com/watch?v=$key"
        else -> ""
    }
}

fun ImageDto.toEntity(): String {
    return "https://image.tmdb.org/t/p/w500$filePath"
}