package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.mapper.actor.getFullImageUrl
import com.sanaa.vod.mapper.actor.toDomain
import com.sanaa.vod.mapper.media.toDomain
import com.sanaa.vod.mapper.media.toDtoId
import com.sanaa.vod.mapper.media.toEntity
import repository.MovieRepository
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import java.net.UnknownHostException

class MovieRepositoryImpl(
    private val remote: RemoteMovieDataSource
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

    override suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie> =
        safeCall("Failed to fetch similar movies") {
            remote.fetchSimilarMoviesByMovieId(id).map { it.toDomain() }
        }

    override suspend fun getReviewsByMovieId(id: Int): List<Review> =
        safeCall("Failed to fetch reviews") {
            remote.fetchReviewsByMovieId(id).map { it.toEntity() }
        }

    override suspend fun getMoviesByCategory(category: Genre): List<Movie> =
        safeCall("Failed to fetch movies by category") {
            remote.fetchMoviesByCategory(category.toDtoId()).map { it.toDomain() }
        }

    override suspend fun getMovieTrailer(id: Int): String? =
        safeCall("Failed to fetch movie trailer") {
            remote.fetchMovieTrailerUrl(id).toDomain()
        }

    override suspend fun getPopularMovies(): List<Movie> {
        return emptyList()
    }

    override suspend fun getTopRatedMovies(genre: Genre): List<Movie> {
        return emptyList()
    }

    override suspend fun getUpcomingMovies(genre: Genre): List<Movie> {
        return emptyList()
    }

    override suspend fun getTrendingMovies(genre: Genre): List<Movie> {
        return emptyList()
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return emptyList()
    }

    private inline fun <T> safeCall(errorMessage: String, block: () -> T): T {
        try {
            return block()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("$errorMessage: ${e.message}")
        }
    }
}