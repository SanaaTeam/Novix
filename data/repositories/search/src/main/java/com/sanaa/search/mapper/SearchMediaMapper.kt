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
        posterImageUrl = getFullImageUrl(imagePath),
    )
}

fun MovieSearchDto.toLocalDto(language: String): MovieLocalDto {
    return MovieLocalDto(
        id = id,
        title = title ?: "",
        imagePath = getFullImageUrl(posterImagePath),
        language = language,
        releaseYear = releaseDate?.takeIf { it.isNotBlank() }?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun MovieSearchDto.toSearchOutput(): SearchMovieOutput {
    return SearchMovieOutput(
        id = id,
        title = title ?: "",
        posterImageUrl = getFullImageUrl(posterImagePath),
    )
}

fun TvSeriesLocalDto.toSearchOutput(): SearchTvSeriesOutput {
    return SearchTvSeriesOutput(
        id = id,
        title = title,
        posterImageUrl = getFullImageUrl(imagePath),
    )
}

fun TvShowSearchDto.toLocalDto(language: String): TvSeriesLocalDto {
    return TvSeriesLocalDto(
        id = id,
        title = name ?: "",
        imagePath = getFullImageUrl(posterImagePath) ,
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
        posterImageUrl = getFullImageUrl(posterImagePath),
    )
}