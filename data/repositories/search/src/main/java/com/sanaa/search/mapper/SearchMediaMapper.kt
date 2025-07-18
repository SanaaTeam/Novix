package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import kotlinx.datetime.LocalDate
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput

fun MovieLocalDto.toSearchOutput(): SearchMovieOutput {
    return SearchMovieOutput(
        id = id,
        title = title,
        posterImageUrl = (IMAGE_URL + imagePath),
    )
}

fun MovieSearchDto.toLocalDto(language: String): MovieLocalDto {
    return MovieLocalDto(
        id = id,
        title = title ?: "",
        imagePath = IMAGE_URL + posterImagePath,
        language = language,
        releaseYear = releaseDate?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun MovieSearchDto.toSearchOutput(): SearchMovieOutput {
    return SearchMovieOutput(
        id = id,
        title = title ?: "",
        posterImageUrl = (IMAGE_URL + posterImagePath),
    )
}

fun TvSeriesLocalDto.toSearchOutput(): SearchTvSeriesOutput {
    return SearchTvSeriesOutput(
        id = id,
        title = title,
        posterImageUrl = (IMAGE_URL + imagePath) ?: "",
    )
}

fun TvShowSearchDto.toLocalDto(language: String): TvSeriesLocalDto {
    return TvSeriesLocalDto(
        id = id,
        title = name ?: "",
        imagePath = IMAGE_URL + posterImagePath,
        language = language,
        releaseYear = releaseDate?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun TvShowSearchDto.toSearchOutput(): SearchTvSeriesOutput {
    return SearchTvSeriesOutput(
        id = id,
        title = name ?: "",
        posterImageUrl = (IMAGE_URL + posterImagePath),
    )
}

const val IMAGE_URL = "https://image.tmdb.org/t/p/original"