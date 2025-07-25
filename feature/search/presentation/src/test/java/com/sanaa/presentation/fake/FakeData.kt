package com.sanaa.presentation.fake

import entity.Actor
import entity.Actor.Gender
import entity.Movie
import entity.TvSeries
import kotlinx.datetime.LocalDate

object FakeData {
    val actorOutputs: List<Actor> = listOf(
        Actor(
            1,
            "Tom Hanks",
            "image.com",
            region = null,
            lastShow = null,
            gender = Gender.MALE,
            department = null,
            character = null,
            birthDate = null,
            deathDate = null,
            placeOfBirth = null,
            biography = null
        ), Actor(
            2,
            "Tom Holland",
            "image.com",
            region = null,
            lastShow = null,
            gender = Gender.MALE,
            department = null,
            character = null,
            birthDate = null,
            deathDate = null,
            placeOfBirth = null,
            biography = null
        )
    )

    val moviesOutput: List<Movie> = listOf(
        Movie(
            1,
            "IronMan1",
            "image.com",
            genres = emptyList(),
            imdbRating = 0f,
            duration = 1,
            releaseDate = LocalDate(1970, 1, 1),
            overview = "",
            trailerUrl = ""
        ), Movie(
            2,
            "IronMan2",
            "image.com",
            genres = emptyList(),
            imdbRating = 0f,
            duration = 1,
            releaseDate = LocalDate(1970, 1, 1),
            overview = "",
            trailerUrl = ""
        )
    )

    val tvShowsOutput: List<TvSeries> = listOf(
        TvSeries(
            1,
            "Tom And Jerry",
            "image.com",
            releaseDate = LocalDate(1970, 1, 1),
            genres = emptyList(),
            imdbRating = 10f,
            posterImageUrl = "",
            seasonsCount = 0
        ), TvSeries(
            2,
            "Friends",
            "image.com",
            releaseDate = LocalDate(1970, 1, 1),
            genres = emptyList(),
            imdbRating = 10f,
            posterImageUrl = "",
            seasonsCount = 0
        )
    )
}