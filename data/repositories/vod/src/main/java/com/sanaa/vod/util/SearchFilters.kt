package com.sanaa.vod.util

import android.util.Log
import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.mapper.search.toEntity
import entity.Movie
import entity.TvSeries
import usecase.search.search_param.MediaFilters

internal fun MediaFilters.filterMovies(movies: List<MovieSearchDto>): List<Movie> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    val minRating = imdbRating
    val start = startYear
    val end = endYear

    return movies.filter { movie ->
        (filterGenreIds == null || byGenres(movie.genreIds, filterGenreIds)) &&
                (minRating == null || byRate(movie.voteAverage, minRating)) &&
                (start == null || byStartYear(movie.releaseDate, start)) &&
                (end == null || byEndYear(movie.releaseDate, end))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterCachedMovies(cachedMovies: List<MovieLocalDto>): List<Movie> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    val minRating = imdbRating
    val start = startYear
    val end = endYear

    return cachedMovies.filter { movie ->
        (filterGenreIds == null || byGenres(movie.genres, filterGenreIds)) &&
                (minRating == null || byRate(movie.imdbRating, minRating)) &&
                (start == null || byStartYear(movie.releaseDate, start)) &&
                (end == null || byEndYear(movie.releaseDate, end))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterTvShows(tvSeries: List<TvShowSearchDto>): List<TvSeries> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    val minRating = imdbRating
    val start = startYear
    val end = endYear

    return tvSeries.filter { tvShow ->
        (filterGenreIds == null || byGenres(tvShow.genreIds, filterGenreIds)) &&
                (minRating == null || byRate(tvShow.voteAverage, minRating)) &&
                (start == null || byStartYear(tvShow.releaseDate, start)) &&
                (end == null || byEndYear(tvShow.releaseDate, end))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterCachedTvShows(cachedTvSeries: List<TvSeriesLocalDto>): List<TvSeries> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    val minRating = imdbRating
    val start = startYear
    val end = endYear

    return cachedTvSeries.filter { tvShow ->
        (filterGenreIds == null || byGenres(tvShow.genres, filterGenreIds)) &&
                (minRating == null || byRate(tvShow.imdbRating, minRating)) &&
                (start == null || byStartYear(tvShow.releaseDate, start)) &&
                (end == null || byEndYear(tvShow.releaseDate, end))
    }.map { it.toEntity() }
}

private fun byStartYear(releaseDate: String?, year: Int): Boolean {
    val parsedYear = releaseDate?.takeIf { it.length >= 4 }
        ?.substring(0, 4)
        ?.toIntOrNull()
    return parsedYear != null && parsedYear >= year
}

private fun byEndYear(releaseDate: String?, year: Int): Boolean {
    val parsedYear = releaseDate?.takeIf { it.length >= 4 }
        ?.substring(0, 4)
        ?.toIntOrNull()
    return parsedYear != null && parsedYear <= year
}

private fun byRate(voteAverage: Float?, rating: Float): Boolean =
    voteAverage != null && voteAverage >= rating

private fun byGenres(genreIds: List<Int>?, selectedGenres: List<Int>): Boolean =
    selectedGenres.all{ it in genreIds.orEmpty() }

private fun byGenres(genreIds: String?, selectedGenres: List<Int>): Boolean {
    val genreSet = genreIds
        ?.split(",")
        ?.mapNotNull { it.trim().toIntOrNull() }
        ?.toSet() ?: emptySet()
    return selectedGenres.all { it in genreSet }
}