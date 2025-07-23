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
    suspend fun fetchMovieTrailerUrl(@Path("movie_id") id: Int): MovieApiResponse<VideoDto>

    @GET("movie/popular")
    @Headers("Ignore-Language: true")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieApiResponse<MovieDto>

    @GET("discover/movie")
    @Headers("Ignore-Language: true")
    suspend fun fetchTrendingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("release_date.gte") minDate: String? = null,
        @Query("release_date.lte") maxDate: String? = null,
    ): MovieApiResponse<MovieDto>

    @GET("discover/movie")
    @Headers("Ignore-Language: true")
    suspend fun fetchTopRatingMovies(
        @Query("page") page: Int,
        @Query("with_genres") withGenres: String? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("sort_by") sortBy: String = "vote_average.desc",
        @Query("vote_count.gte") voteCountGte: Int = 100,

        ): MovieApiResponse<MovieDto>

    @GET("discover/movie")
    suspend fun fetchUpcomingMovies(
        @Query("page") page: Int,
        @Query("with_genres") genreId: Int? = null,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("with_genres") withGenres: String? = null,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("with_release_type") releaseType: String = "2|3",
        @Query("release_date.gte") minDate: String? = null,
        @Query("release_date.lte") maxDate: String? = null
    ): MovieApiResponse<MovieDto>

    @GET("genre/movie/list")
    @Headers("Ignore-Language: true")
    suspend fun fetchMovieGenres(): MovieApiResponse<GenreDto>

}