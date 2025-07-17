package com.sanaa.movies.repository

import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.mapper.fullImageUrlOrEmpty
import com.sanaa.movies.mapper.toDomain
import com.sanaa.movies.mapper.toDtoId
import details.repository.MovieRepository
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import java.net.UnknownHostException

class MovieRepositoryImpl(
    private val remote: MovieDetailsRemoteDataSource
) : MovieRepository {
    override suspend fun getMovieDetails(id: Int): Movie =
        safeCall("Failed to fetch movie details") {
            remote.fetchMovieDetails(id).toDomain()
        }

    override suspend fun getImagesUrls(id: Int): List<String> =
        safeCall("Failed to fetch images") {
        remote.fetchImagesUrl(id).posters.take(3).map { it.filePath.fullImageUrlOrEmpty() }
    }

    override suspend fun getMovieCast(id: Int): List<Actor> =
        safeCall("Failed to fetch movie cast") {
            remote.fetchCast(id).cast.map { it.toDomain() }
        }

    override suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie> =
        safeCall("Failed to fetch similar movies") {
            remote.fetchSimilarMoviesByMovieId(id).results.map { it.toDomain() }
        }

    override suspend fun getReviewsByMovieId(id: Int): List<Review> =
        safeCall("Failed to fetch reviews") {
            remote.fetchReviewsByMovieId(id).results.map { it.toDomain() }
        }

    override suspend fun getMoviesByCategory(category: Genre): List<Movie> =
        safeCall("Failed to fetch movies by category") {
            remote.fetchMoviesByCategory(category.toDtoId()).results.map { it.toDomain() }
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