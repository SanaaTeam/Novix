package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalCachedContentDataSource
import com.sanaa.vod.dataSource.local.cache.dto.CachedContentMetadataLocalDto.Category
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.repository.mapper.cachedContent.toDomain
import com.sanaa.vod.repository.mapper.cachedContent.toLocalDto
import com.sanaa.vod.repository.mapper.media.getFullImageUrl
import com.sanaa.vod.repository.mapper.media.toDomain
import com.sanaa.vod.repository.mapper.media.toEntity
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
    private val localCachedContentDataSource: LocalCachedContentDataSource,
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
            if (page == 1) {
                val cachedMovies =
                    localCachedContentDataSource.getCachedMovies(category = Category.POPULAR)
                if (cachedMovies.isNotEmpty()) {
                    return cachedMovies.map { it.toDomain() }
                }
            }

            return remote.fetchPopularMovies(page).map { it.toDomain() }.also {
                if (page == 1) {
                    localCachedContentDataSource.cacheMovie(
                        movie = it.map { it.toLocalDto() },
                        category = Category.POPULAR
                    )
                }
            }
        }


    override suspend fun getTopRatedMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie TopRated") {
            if (page == 1 && genreId == null) {
                val cachedMovies =
                    localCachedContentDataSource.getCachedMovies(Category.TOP_RATED)
                if (cachedMovies.isNotEmpty()) {
                    return cachedMovies.map { it.toDomain() }
                }
            }
            return remote.fetchTopRatedMovies(page, genreId).map { it.toDomain() }.also {
                if (page == 1 && genreId == null) {
                    localCachedContentDataSource.cacheMovie(
                        movie = it.map { it.toLocalDto() },
                        category = Category.TOP_RATED
                    )
                }
            }
        }

    override suspend fun getUpcomingMovies(page: Int, genreId: Int?): List<Movie> =
        safeCall("Failed to fetch movie Upcoming") {
            if (page == 1 && genreId == null) {
                val cachedMovies =
                    localCachedContentDataSource.getCachedMovies(Category.UPCOMING)
                if (cachedMovies.isNotEmpty()) {
                    return cachedMovies.map { it.toDomain() }
                }
            }

            return remote.fetchUpcomingMovies(page, genreId).map { it.toDomain() }.also {
                if (page == 1 && genreId == null) {
                    localCachedContentDataSource.cacheMovie(
                        movie = it.map { it.toLocalDto() },
                        category = Category.UPCOMING
                    )
                }
            }
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