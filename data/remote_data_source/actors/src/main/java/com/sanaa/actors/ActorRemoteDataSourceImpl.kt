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

    private suspend inline fun <reified T> fetch(
        path: String,
        withLang: Boolean = true
    ): T = client.get("$baseUrl/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body()


    override suspend fun getActorDetails(actorId: Int): ActorDto =
        fetch("person/$actorId")

    override suspend fun getActorImages(actorId: Int): ActorImagesDto =
        fetch("person/$actorId/images", withLang = false)

    override suspend fun getActorTopMovies(actorId: Int): ActorMovieCastDto =
        fetch("person/$actorId/movie_credits")

    override suspend fun getActorTopTvSeries(actorId: Int): ActorTvCastDto =
        fetch("person/$actorId/tv_credits")
}

