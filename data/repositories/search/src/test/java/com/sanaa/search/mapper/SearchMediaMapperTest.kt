package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto

class SearchMediaMapperTest {

    private fun createMovieSearchDto(
        id: Int = 1,
        title: String? = null,
        posterImagePath: String? = null,
        releaseDate: String? = null,
        genreIds: List<Int>? = null,
        voteAverage: Float? = null
    ) = MovieSearchDto(
        id = id,
        title = title,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate,
        genreIds = genreIds,
        voteAverage = voteAverage
    )

    private fun createMovieLocalDto(
        id: Int = 1,
        title: String = "",
        imagePath: String? = null,
        language: String = "en",
        releaseYear: Int? = null,
        genres: String? = null,
        imdbRating: Float? = null
    ) = MovieLocalDto(
        id = id,
        title = title,
        imagePath = imagePath,
        language = language,
        releaseYear = releaseYear,
        genres = genres,
        imdbRating = imdbRating
    )

    private fun createTvShowSearchDto(
        id: Int = 1,
        name: String? = null,
        posterImagePath: String? = null,
        releaseDate: String? = null,
        genreIds: List<Int>? = null,
        voteAverage: Float? = null
    ) = TvShowSearchDto(
        id = id,
        name = name,
        posterImagePath = posterImagePath,
        releaseDate = releaseDate,
        genreIds = genreIds,
        voteAverage = voteAverage
    )

    private fun createTvSeriesLocalDto(
        id: Int = 1,
        title: String = "",
        imagePath: String? = null,
        language: String = "en",
        releaseYear: Int? = null,
        genres: String? = null,
        imdbRating: Float? = null
    ) = TvSeriesLocalDto(
        id = id,
        title = title,
        imagePath = imagePath,
        language = language,
        releaseYear = releaseYear,
        genres = genres,
        imdbRating = imdbRating
    )
}