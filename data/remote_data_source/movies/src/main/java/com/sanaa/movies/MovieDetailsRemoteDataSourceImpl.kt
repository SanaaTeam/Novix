package com.sanaa.movies

import com.example.env_config.service.LanguageProvider
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.sanaa.movies.dataSource.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.dto.ActorDto
import com.sanaa.movies.dataSource.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.dto.ReviewDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class MovieDetailsRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
): MovieDetailsRemoteDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDetailsDto {
        return client.get("$baseUrl/movie/$id") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

    override suspend fun fetchImages(id: Int): List<String> {
        return client.get("$baseUrl/movie/$id/images") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)

        }
    }

    override suspend fun fetchCast(id: Int): List<ActorDto> {
        return client.get("$baseUrl/movie/$id/credits") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)

        }
    }

    override suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDetailsDto> {
        return client.get("$baseUrl/movie/$id/similar") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)

        }
    }

    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> {
        return client.get("$baseUrl/movie/$id/reviews") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }
    }

    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDetailsDto> {
        return client.get("$baseUrl/movie") {
            parameter ("with_genres", category)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)

        }
    }

}