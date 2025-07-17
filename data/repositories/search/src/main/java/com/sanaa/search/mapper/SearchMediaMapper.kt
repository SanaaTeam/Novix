package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.MovieLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import kotlinx.datetime.LocalDate
import search.usecase.search_param.SearchMovieOutput
import search.usecase.search_param.SearchTvSeriesOutput

fun MoviesLocalDto.toSearchOutput(): SearchMovieOutput {
    return SearchMovieOutput(
        id = id,
        title = title,
        posterImageUrl = (imageUrl + imagePath),
    )
}

fun MovieSearchDto.toLocalDto(language: String): MovieLocalDto {
    return MovieLocalDto(
        id = id,
        title = title ?: "",
        imagePath = imageUrl + posterImagePath,
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
        posterImageUrl = imageUrl + posterImagePath ?: "",
    )
}

fun TvSeriesLocalDto.toSearchOutput(): SearchTvSeriesOutput {
    return SearchTvSeriesOutput(
        id = id,
        title = title,
        posterImageUrl = (imageUrl + imagePath) ?: "",
    )
}

fun TvShowSearchDto.toLocalDto(language: String): TvSeriesLocalDto {
    return TvSeriesLocalDto(
        id = id,
        title = name ?: "",
        imagePath = imageUrl + posterImagePath,
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
        posterImageUrl = imageUrl + posterImagePath ?: "",
    )
}

val imageUrl = "https://image.tmdb.org/t/p/original"