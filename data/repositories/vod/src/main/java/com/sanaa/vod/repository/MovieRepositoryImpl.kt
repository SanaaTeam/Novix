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
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val preferences: PreferencesManager
) : MovieRepository {
    override suspend fun getMovieDetails(id: Int): Movie =
        safeCall("Failed to fetch movie details") {
            remoteMovieDataSource.fetchMovieDetails(id).toDomain()
        }

    override suspend fun getImageUrls(id: Int, count: Int): List<String> =
        safeCall("Failed to fetch images") {
            remoteMovieDataSource.fetchImagesUrl(id).take(count).map { getFullImageUrl(it.filePath) }
        }

    override suspend fun getMovieCast(id: Int): List<Actor> =
        safeCall("Failed to fetch movie cast") {
            remoteMovieDataSource.fetchCast(id).map { it.toDomain() }
        }

    override suspend fun getSimilarMoviesByMovieId(id: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch similar movies") {
            remoteMovieDataSource.fetchSimilarMoviesByMovieId(id, page).map { it.toDomain() }
        }

    override suspend fun getReviewsByMovieId(id: Int, page: Int): List<Review> =
        safeCall("Failed to fetch reviews") {
            remoteMovieDataSource.fetchReviewsByMovieId(id, page).map { it.toEntity() }
        }

    override suspend fun getMoviesByCategory(genreId: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch movies by category") {
            remoteMovieDataSource.fetchMoviesByCategory(
                genreId, page
            ).map { it.toDomain() }
        }

    override suspend fun getMovieTrailer(id: Int): String? =
        safeCall("Failed to fetch movie trailer") {
            remoteMovieDataSource.fetchMovieTrailerUrl(id).toDomain()
        }


    override suspend fun getPopularMovies(page: Int): List<Movie> =
        safeCall("Failed to fetch movie Popular") {
            remoteMovieDataSource.fetchPopularMovies(page).map { it.toDomain() }
        }


    override suspend fun getTopRatedMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie TopRated") {
            remoteMovieDataSource.fetchTopRatedMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getUpcomingMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie Upcoming") {
            remoteMovieDataSource.fetchUpcomingMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getTrendingMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie Trending") {
            remoteMovieDataSource.fetchTrendingMovies(page, genreId).map { it.toDomain() }
        }

    override suspend fun getMoviesRate(accountId: Long): List<Movie> {
        return safeCall("Failed to fetch movie Rate") {
            val sessionId = preferences.sessionId.first()
            remoteMovieDataSource.fetchMoviesRate(accountId = accountId, sessionId = sessionId)
                .map { it.toDomain() }
        }
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return safeCall("Failed to fetch movie genres") {
            remoteMovieDataSource.fetchMovieGenres().map { it.toEntity() }
        }
    }

    override suspend fun addMovieRate(movieId: Int, rating: Float): Boolean {
        return safeCall("Failed to add movie rate") {
            val sessionId = preferences.sessionId.first()
            remoteMovieDataSource.sendMovieRate(
                movieId = movieId,
                sessionId = sessionId,
                rating = rating
            ).isSuccess
        }
    }
}