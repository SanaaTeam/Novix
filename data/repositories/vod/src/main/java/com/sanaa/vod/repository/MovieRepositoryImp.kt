package com.sanaa.vod.repository

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
import repository.MovieRepository

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
            remote.fetchMoviesByCategory(category.id).map { it.toDomain() }
        }

    override suspend fun getMovieTrailer(id: Int): String? =
        safeCall("Failed to fetch movie trailer") {
            remote.fetchMovieTrailerUrl(id).toDomain()
        }


    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return emptyList()
    }

    override suspend fun getTopRatedMovies(page: Int, genre: Genre?): List<Movie> {
        return emptyList()
    }

    override suspend fun getUpcomingMovies(page: Int, genre: Genre?): List<Movie> {
        return emptyList()
    }

    override suspend fun getTrendingMovies(page: Int, genre: Genre?): List<Movie> {
        return emptyList()
    }

    override suspend fun getMovieGenres(): List<Genre> {
        return emptyList()
    }
}