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


fun VideoDto.toEntity(): String {
    return when (site) {
        "YouTube" -> "https://www.youtube.com/watch?v=$key"
        else -> ""
    }
}

fun GenreDto.toEntity(): Genre {
    return when (id) {
        10759 -> Genre.ACTION_AND_ADVENTURE
        16 -> Genre.ANIMATION
        35 -> Genre.COMEDY
        80 -> Genre.CRIME
        99 -> Genre.DOCUMENTARY
        18 -> Genre.DRAMA
        10751 -> Genre.FAMILY
        10762 -> Genre.KIDS
        9648 -> Genre.MYSTERY
        10763 -> Genre.NEWS
        10764 -> Genre.REALITY
        10765 -> Genre.SCI_FI_AND_FANTASY
        10766 -> Genre.SOAP
        10767 -> Genre.TALK
        10768 -> Genre.WAR_AND_POLITICS
        37 -> Genre.WESTERN
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