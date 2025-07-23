package com.sanaa.vod.media.actor

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.util.wrapApiCall

class RemoteActorDataSourceImpl(
    private val apiService: ActorApiService
) : RemoteActorDataSource {

    override suspend fun getActorDetails(actorId: Int): ActorDto =
        wrapApiCall { apiService.fetchActorDetails(actorId) }

    override suspend fun getActorImages(actorId: Int): List<ImageDto> = wrapApiCall {
        apiService.fetchActorImages(actorId).profiles
    }

    override suspend fun getActorMovies(actorId: Int): List<ActorCastCreditDto> =
        wrapApiCall { apiService.fetchActorMovies(actorId).cast }

    override suspend fun getActorTvShows(actorId: Int): List<ActorCastCreditDto> =
        wrapApiCall { apiService.fetchActorTvShows(actorId).cast }
}