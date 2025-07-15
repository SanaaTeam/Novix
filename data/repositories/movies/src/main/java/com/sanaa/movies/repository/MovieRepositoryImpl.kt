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
    override suspend fun getMovieDetails(id: Int): Movie {
        try {
            return remote.fetchMovieDetails(id).toDomain()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch movie details: ${e.message}")
        }
    }


    override suspend fun getImages(id: Int): List<String> {
        try {
            return remote.fetchImages(id).posters.take(3)
                .map { it.filePath.fullImageUrlOrEmpty() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch images: ${e.message}")
        }
    }

    override suspend fun getMovieCast(id: Int): List<Actor> {
        try {
            return remote.fetchCast(id).cast.map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch movie cast")
        }
    }

    override suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie> {
        try {
            return remote.fetchSimilarMoviesByMovieId(id).results.map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch similar movies")
        }
    }

    override suspend fun getReviewsByMovieId(id: Int): List<Review> {
        try {
            return remote.fetchReviewsByMovieId(id).results.map { it.toDomain() }
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch reviews")
        }
    }

    override suspend fun getMoviesByCategory(category: Genre): List<Movie> {
        TODO()
    }

}