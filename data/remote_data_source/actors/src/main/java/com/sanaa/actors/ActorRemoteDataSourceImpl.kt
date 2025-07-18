package com.sanaa.actors

import com.example.preferences.service.LanguageProvider
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.actors.dataSource.remote.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class ActorRemoteDataSourceImpl(
    private val client: HttpClient,
    private val languageProvider: LanguageProvider
) : ActorRemoteDataSource {

    override suspend fun getActorDetails(actorId: Int): ActorDto =
        fetch("person/$actorId")

    override suspend fun getActorImages(actorId: Int): ActorImagesDto =
        fetch("person/$actorId/images", withLang = false)

    override suspend fun getActorTopMovies(actorId: Int): ActorMovieCastDto =
        fetch("person/$actorId/movie_credits")

    override suspend fun getActorTopTvSeries(actorId: Int): ActorTvCastDto =
        fetch("person/$actorId/tv_credits")

    private suspend inline fun <reified T> fetch(
        path: String,
        withLang: Boolean = true
    ): T = fetchInternal(path, withLang, typeInfo<T>())

    private suspend fun <T> fetchInternal(
        path: String,
        withLang: Boolean,
        typeInfo: TypeInfo
    ): T = client.get("${BuildConfig.TMDB_URL}/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body(typeInfo)
}
