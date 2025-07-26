package com.sanaa.vod.media.movie

import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.media.movie.response.MovieApiResponse
import com.sanaa.vod.media.movie.response.MovieCastResponse
import com.sanaa.vod.media.movie.response.MovieImagesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("3/movie/{movie_id}")
    suspend fun fetchMovieDetails(@Path("movie_id") id: Int): MovieDto

    @GET("3/movie/{movie_id}/images")
    @Headers("Ignore-Language: true")
    suspend fun fetchImagesUrl(@Path("movie_id") id: Int): MovieImagesResponse

    @GET("3/movie/{movie_id}/credits")
    suspend fun fetchCast(@Path("movie_id") id: Int): MovieCastResponse

    @GET("3/movie/{movie_id}/similar")
    @Headers("Ignore-Language: true")
    suspend fun fetchSimilarMoviesByMovieId(@Path("movie_id") id: Int): MovieApiResponse<MovieDto>

    @GET("3/movie/{movie_id}/reviews")
    suspend fun fetchReviewsByMovieId(@Path("movie_id") id: Int): MovieApiResponse<ReviewDto>

    @GET("3/discover/movie")
    @Headers("Ignore-Language: true")
    suspend fun fetchMoviesByCategory(@Query("with_genres") category: Int): MovieApiResponse<MovieDto>

    @GET("3/movie/{movie_id}/videos")
    @Headers("Ignore-Language: true")
    suspend fun fetchMovieTrailerUrl(@Path("movie_id") id: Int): MovieApiResponse<VideoDto>

    @GET("3/genre/movie/list")
    @Headers("Ignore-Language: true")
    suspend fun fetchMovieGenres(): MovieApiResponse<GenreDto>

}