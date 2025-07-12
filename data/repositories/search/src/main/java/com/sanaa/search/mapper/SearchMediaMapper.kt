package com.sanaa.search.mapper

import com.sanaa.search.dataSource.local.dto.MoviesLocalDto
import com.sanaa.search.dataSource.local.dto.TvSeriesLocalDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import kotlinx.datetime.LocalDate
import usecase.search.SearchMediaOutput

fun MoviesLocalDto.toSearchOutput(isSaved: Boolean): SearchMediaOutput {
    return SearchMediaOutput(
        id = id,
        title = title,
        posterImageUrl = imagePath ?: "",
        isSaved = isSaved,
    )
}

fun MovieSearchDto.toLocalDto(language: String): MoviesLocalDto {
    return MoviesLocalDto(
        id = id,
        title = title ?: "",
        imagePath = posterImagePath,
        language = language,
        releaseYear = releaseDate?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun MovieSearchDto.toSearchOutput(isSaved: Boolean): SearchMediaOutput {
    return SearchMediaOutput(
        id = id,
        title = title ?: "",
        posterImageUrl = posterImagePath ?: "",
        isSaved = isSaved
    )
}

fun TvSeriesLocalDto.toSearchOutput(isSaved: Boolean): SearchMediaOutput {
    return SearchMediaOutput(
        id = id,
        title = title,
        posterImageUrl = imagePath ?: "",
        isSaved = isSaved,
    )
}

fun TvShowSearchDto.toLocalDto(language: String): TvSeriesLocalDto {
    return TvSeriesLocalDto(
        id = id,
        title = name ?: "",
        imagePath = posterImagePath,
        language = language,
        releaseYear = releaseDate?.let { LocalDate.parse(it).year },
        genres = genreIds?.joinToString(separator = ", "),
        imdbRating = voteAverage,
    )
}

fun TvShowSearchDto.toSearchOutput(isSaved: Boolean): SearchMediaOutput {
    return SearchMediaOutput(
        id = id,
        title = name ?: "",
        posterImageUrl = posterImagePath ?: "",
        isSaved = isSaved
    )
}