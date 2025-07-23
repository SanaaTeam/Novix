package com.sanaa.vod.util

import com.sanaa.vod.dataSource.local.search.dto.MovieLocalDto
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.mapper.media.toDtoId
import com.sanaa.vod.mapper.search.toSearchOutput
import kotlinx.datetime.LocalDate
import usecase.search.search_param.MediaFilters
import usecase.search.search_param.SearchMovieOutput
import usecase.search.search_param.SearchTvSeriesOutput

internal fun MediaFilters.filterMovies(movies: List<MovieSearchDto>): List<SearchMovieOutput> {
    val filterGenreIds = if (genres.isEmpty()) null else genres.map { it.toDtoId() }
    return movies
        .filterNonNull(filterGenreIds) { movie, ids -> byGenres(movie.genreIds, ids) }
        .filterNonNull(imdbRating) { movie, rating -> byRate(movie.voteAverage, rating) }
        .filterNonNull(startYear) { movie, year -> byStartYear(movie.releaseDate, year) }
        .filterNonNull(endYear) { movie, year -> byEndYear(movie.releaseDate, year) }
        .map { it.toSearchOutput() }
}


internal fun MediaFilters.filterCashedMovies(
    cachedMovies: List<MovieLocalDto>,
): List<SearchMovieOutput> {
    val filterGenreIds = if (genres.isEmpty()) null else genres.map { it.toDtoId() }
    return cachedMovies
        .filterNonNull(filterGenreIds) { movie, ids -> byGenres(movie.genres, ids) }
        .filterNonNull(imdbRating) { movie, rating -> byRate(movie.imdbRating, rating) }
        .filterNonNull(startYear) { movie, year -> byStartYear(movie.releaseYear, year) }
        .filterNonNull(endYear) { movie, year -> byEndYear(movie.releaseYear, year) }
        .map { it.toSearchOutput() }
}

internal fun MediaFilters.filterTvShows(
    tvSeries: List<TvShowSearchDto>,
): List<SearchTvSeriesOutput> {
    let {
        val filterGenreIds = if (genres.isEmpty()) null else genres.map { it.toDtoId() }
        return tvSeries
            .filterNonNull(filterGenreIds) { movie, ids -> byGenres(movie.genreIds, ids) }
            .filterNonNull(imdbRating) { movie, rating -> byRate(movie.voteAverage, rating) }
            .filterNonNull(startYear) { movie, year -> byStartYear(movie.releaseDate, year) }
            .filterNonNull(endYear) { movie, year -> byEndYear(movie.releaseDate, year) }
            .map { it.toSearchOutput() }
    }
}

internal fun MediaFilters.filterCashedTvShows(
    cachedTvSeries: List<TvSeriesLocalDto>,
): List<SearchTvSeriesOutput> {
    let {
        val filterGenreIds = if (genres.isEmpty()) null else genres.map { it.toDtoId() }
        return cachedTvSeries
            .filterNonNull(filterGenreIds) { movie, ids -> byGenres(movie.genres, ids) }
            .filterNonNull(imdbRating) { movie, rating -> byRate(movie.imdbRating, rating) }
            .filterNonNull(startYear) { movie, year -> byStartYear(movie.releaseYear, year) }
            .filterNonNull(endYear) { movie, year -> byEndYear(movie.releaseYear, year) }
            .map { it.toSearchOutput() }
    }
}


private fun byStartYear(releaseDate: String?, year: Int): Boolean {
    return releaseDate?.let { LocalDate.parse(releaseDate).year >= year } == true
}

private fun byStartYear(releaseYear: Int?, year: Int): Boolean {
    return releaseYear != null && releaseYear >= year
}

private fun byEndYear(releaseDate: String?, year: Int): Boolean {
    return releaseDate?.let { LocalDate.parse(releaseDate).year <= year } == true
}

private fun byEndYear(releaseYear: Int?, year: Int): Boolean {
    return releaseYear != null && releaseYear <= year
}

private fun byRate(voteAverage: Float?, rating: Float): Boolean {
    return (voteAverage ?: 0f) >= rating
}

private fun byGenres(genreIds: List<Int>?, ids: List<Int>): Boolean {
    return genreIds?.let { genreIds ->
        ids.all { id -> id in genreIds }
    } == true
}

private fun byGenres(genreIds: String?, ids: List<Int>): Boolean {
    val genreIds = genreIds?.split(", ")
        ?.mapNotNull { it.toIntOrNull() }
        ?.toSet() ?: emptySet()
    return ids.all { it in genreIds }
}

fun <T, R> List<R>.filterNonNull(
    value: T?,
    action: (movie: R, value: T) -> Boolean,
): List<R> {
    return if (value == null)
        this
    else
        filter { action(it, value) }
}