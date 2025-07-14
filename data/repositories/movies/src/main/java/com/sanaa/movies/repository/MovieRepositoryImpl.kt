package com.sanaa.movies.repository

import com.sanaa.movies.dataSource.MovieDetailsRemoteDataSource
import com.sanaa.movies.mapper.toDomain
import com.sanaa.movies.mapper.toDtoId
import details.repository.MovieRepository
import entity.Actor
import entity.Genre
import entity.Movie
import entity.Review
import exceptions.RetrievingDataFailureException

class MovieRepositoryImpl(
    private val remote: MovieDetailsRemoteDataSource
) : MovieRepository {
    override suspend fun getMovieDetails(id: Int): Movie {
        try {
            return remote.fetchMovieDetails(id).toDomain()
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch movie details")
        }
    }


    override suspend fun getImages(id: Int): List<String> {
        try {
            return remote.fetchImages(id)
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch images")
        }
    }

    override suspend fun getMovieCast(id: Int): List<Actor> {
        try {
            return remote.fetchCast(id).map { it.toDomain() }
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch movie cast")
        }
    }

    override suspend fun getSimilarMoviesByMovieId(id: Int): List<Movie> {
        try {
            return remote.fetchSimilarMoviesByMovieId(id).map { it.toDomain() }
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch similar movies")
        }
    }

    override suspend fun getReviewsByMovieId(id: Int): List<Review> {
        try {
            return remote.fetchReviewsByMovieId(id).map { it.toDomain() }
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch reviews")
        }
    }

    override suspend fun getMoviesByCategory(category: Genre): List<Movie> {
        try {
            return remote.fetchMoviesByCategory(category.toDtoId()).map { it.toDomain() }
        } catch (e: Exception) {
            throw RetrievingDataFailureException("Failed to fetch movies by category")
        }
    }

}