package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import kotlinx.datetime.LocalDate
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput

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
        title = name.orEmpty(),
        imagePath = getFullImageUrl(posterImagePath),
        language = language,
        releaseYear = releaseDate?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun TvShowSearchDto.toSearchOutput(): SearchTvSeriesOutput {
    return SearchTvSeriesOutput(
        id = id,
        title = name.orEmpty(),
        posterImageUrl = getFullImageUrl(posterImagePath),
    )
}