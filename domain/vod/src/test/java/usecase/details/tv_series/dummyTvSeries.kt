package usecase.details.tv_series

import entity.Genre
import entity.TvSeries
import extensions.now
import kotlinx.datetime.LocalDateTime

val dummyTvSeries =
    TvSeries(
        id = 1,
        title = "The Walking Dead",
        overview = "",
        releaseDate = LocalDateTime.now().date,
        genres = listOf(Genre.WAR_AND_POLITICS),
        imdbRating = 1.2f,
        posterImageUrl = "Image",
        seasonsCount = 5,
        trailerUrl = "Url",
    )