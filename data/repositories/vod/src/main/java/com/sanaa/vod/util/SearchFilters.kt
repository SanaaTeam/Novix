package com.sanaa.vod.util

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
    return movies.filter { movie ->
        (filterGenreIds == null || byGenres(movie.genreIds, filterGenreIds)) &&
                (byRate(movie.voteAverage, imdbRating)) &&
                (byStartYear(movie.releaseDate, startYear)) &&
                (byEndYear(movie.releaseDate, endYear))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterCachedMovies(cachedMovies: List<MovieLocalDto>): List<Movie> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    return cachedMovies.filter { movie ->
        (filterGenreIds == null || byGenres(movie.genres, filterGenreIds)) &&
                (byRate(movie.imdbRating, imdbRating)) &&
                (byStartYear(movie.releaseDate, startYear)) &&
                (byEndYear(movie.releaseDate, endYear))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterTvShows(tvSeries: List<TvShowSearchDto>): List<TvSeries> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    return tvSeries.filter { tvShow ->
        (filterGenreIds == null || byGenres(tvShow.genreIds, filterGenreIds)) &&
                (byRate(tvShow.voteAverage, imdbRating)) &&
                (byStartYear(tvShow.releaseDate, startYear)) &&
                (byEndYear(tvShow.releaseDate, endYear))
    }.map { it.toEntity() }
}

internal fun MediaFilters.filterCachedTvShows(cachedTvSeries: List<TvSeriesLocalDto>): List<TvSeries> {
    val filterGenreIds = genres.takeIf { it.isNotEmpty() }?.map { it.id }
    return cachedTvSeries.filter { tvShow ->
        (filterGenreIds == null || byGenres(tvShow.genres, filterGenreIds)) &&
                (byRate(tvShow.imdbRating, imdbRating)) &&
                (byStartYear(tvShow.releaseDate, startYear)) &&
                (byEndYear(tvShow.releaseDate, endYear))
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

private fun byGenres(genreIds: List<Int>?, ids: List<Int>): Boolean =
    genreIds?.any { it in ids } == true

private fun byGenres(genreIds: String?, ids: List<Int>): Boolean {
    val genreSet = genreIds
        ?.split(",")
        ?.mapNotNull { it.trim().toIntOrNull() }
        ?.toSet() ?: emptySet()
    return ids.any { it in genreSet }
}