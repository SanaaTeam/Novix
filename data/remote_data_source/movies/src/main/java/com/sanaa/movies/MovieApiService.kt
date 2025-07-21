package com.sanaa.movies

import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.response.MovieApiResponse
import com.sanaa.movies.response.MovieCastResponse
import com.sanaa.movies.response.MovieImagesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(@Path("movie_id") id: Int): MovieDto

    @GET("movie/{movie_id}/images")
    @Headers("Ignore-Language: true")
    suspend fun fetchImagesUrl(@Path("movie_id") id: Int): MovieImagesResponse

    @GET("movie/{movie_id}/credits")
    suspend fun fetchCast(@Path("movie_id") id: Int): MovieCastResponse

    @GET("movie/{movie_id}/similar")
    @Headers("Ignore-Language: true")
    suspend fun fetchSimilarMoviesByMovieId(@Path("movie_id") id: Int): MovieApiResponse<MovieDto>

    @GET("movie/{movie_id}/reviews")
    suspend fun fetchReviewsByMovieId(@Path("movie_id") id: Int): MovieApiResponse<ReviewDto>

    @GET("discover/movie")
    @Headers("Ignore-Language: true")
    suspend fun fetchMoviesByCategory(@Query("with_genres") category: Int): MovieApiResponse<MovieDto>

    @GET("movie/{movie_id}/videos")
    @Headers("Ignore-Language: true")
    suspend fun fetchMovieTrailerUrl(@Path("movie_id") id: Int): MovieApiResponse<MovieVideoDto>
}