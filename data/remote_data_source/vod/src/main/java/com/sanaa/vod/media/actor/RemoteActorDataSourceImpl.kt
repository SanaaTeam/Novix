package com.sanaa.vod.media.actor

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.vod.BuildConfig
import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.media.actor.response.ActorActorCastCreditResponse
import com.sanaa.vod.media.actor.response.ActorImagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class RemoteActorDataSourceImpl(
    private val client: HttpClient,
    private val languageProvider: LanguageProvider
) : RemoteActorDataSource {

    override suspend fun getActorDetails(actorId: Int): ActorDto =
        fetch("person/$actorId")

    override suspend fun getActorImages(actorId: Int): List<ImageDto> =
        fetch<ActorImagesResponse>("person/$actorId/images", withLang = false).profiles

    override suspend fun getActorTopMovies(actorId: Int): List<ActorCastCreditDto> =
        fetch<ActorActorCastCreditResponse>("person/$actorId/movie_credits").cast

    override suspend fun getActorTopTvSeries(actorId: Int): List<ActorCastCreditDto> =
        fetch<ActorActorCastCreditResponse>("person/$actorId/tv_credits").cast

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