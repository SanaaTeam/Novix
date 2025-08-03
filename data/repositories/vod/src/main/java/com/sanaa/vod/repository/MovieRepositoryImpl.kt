package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.mapper.actor.getFullImageUrl
import com.sanaa.vod.mapper.actor.toDomain
import com.sanaa.vod.mapper.media.toDomain
import com.sanaa.vod.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import kotlinx.coroutines.flow.first
import repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remote: RemoteMovieDataSource,
    private val preferences: PreferencesManager
) : MovieRepository {
    override suspend fun getMovieDetails(id: Int): Movie =
        safeCall("Failed to fetch movie details") {
            remote.fetchMovieDetails(id).toDomain()
        }

    override suspend fun getImageUrls(id: Int, count: Int): List<String> =
        safeCall("Failed to fetch images") {
            remote.fetchImagesUrl(id).take(count).map { getFullImageUrl(it.filePath) }
        }

    override suspend fun getMovieCast(id: Int): List<Actor> =
        safeCall("Failed to fetch movie cast") {
            remote.fetchCast(id).map { it.toDomain() }
        }

    override suspend fun getSimilarMoviesByMovieId(id: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch similar movies") {
            remote.fetchSimilarMoviesByMovieId(id, page).map { it.toDomain() }
        }

    override suspend fun getReviewsByMovieId(id: Int, page: Int): List<Review> =
        safeCall("Failed to fetch reviews") {
            remote.fetchReviewsByMovieId(id, page).map { it.toEntity() }
        }

    override suspend fun getMoviesByCategory(genreId: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch movies by category") {
            remote.fetchMoviesByCategory(
                genreId, page
            ).map { it.toDomain() }
        }

    override suspend fun getMovieTrailer(id: Int): String? =
        safeCall("Failed to fetch movie trailer") {
            remote.fetchMovieTrailerUrl(id).toDomain()
        }


    override suspend fun getPopularMovies(page: Int): List<Movie> =
        safeCall("Failed to fetch movie Popular") {
            remote.fetchPopularMovies(page).map { it.toDomain() }
        }


    override suspend fun getTopRatedMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie TopRated") {
            remote.fetchTopRatedMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getUpcomingMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie Upcoming") {
            remote.fetchUpcomingMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getTrendingMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie Trending") {
            remote.fetchTrendingMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getMovieRate(accountId: Long, movieId: Int): Int? {
        return safeCall("Failed to fetch movie Rate") {
            val sessionId = preferences.sessionId.first()
            remote.fetchMoviesRate(accountId = accountId, sessionId = sessionId)
                .map { it.toDomain() }.find { it.id == movieId }?.rating
        }
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return safeCall("Failed to fetch movie genres") {
            remote.fetchMovieGenres().map { it.toEntity() }
        }
    }

    override suspend fun addMovieRate(movieId: Int, rating: Float): Boolean {
        return safeCall("Failed to add movie rate") {
            val sessionId = preferences.sessionId.first()
            remote.sendMovieRate(
                movieId = movieId,
                sessionId = sessionId,
                rating = rating
            ).isSuccess
        }
    }
}