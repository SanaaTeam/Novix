package com.sanaa.actors

import com.example.env_config.service.LanguageProvider
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.actors.dataSource.remote.dto.ActorDto
import com.sanaa.actors.dataSource.remote.dto.ActorImagesDto
import com.sanaa.actors.dataSource.remote.dto.ActorMovieCastDto
import com.sanaa.actors.dataSource.remote.dto.ActorTvCastDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ActorRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
) : ActorRemoteDataSource {

    override suspend fun getActorDetails(actorId: Int): ActorDto {
        return client.get("$baseUrl/person/$actorId") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

    override suspend fun getActorImages(actorId: Int): ActorImagesDto {
        return client.get("$baseUrl/person/$actorId/images") {
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

    override suspend fun getActorTopMovies(actorId: Int): ActorMovieCastDto {
        return client.get("$baseUrl/person/$actorId/movie_credits") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

    override suspend fun getActorTopTvSeries(actorId: Int): ActorTvCastDto {
        return client.get("$baseUrl/person/$actorId/tv_credits") {
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }
}
