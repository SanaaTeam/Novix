package com.sanaa.vod.media.actor

import com.sanaa.vod.dataSource.remote.actor.RemoteActorDataSource
import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto

class RemoteActorDataSourceImpl(
    private val apiService: ActorApiService
) : RemoteActorDataSource {

    override suspend fun getActorDetails(actorId: Int): ActorDto =
        apiService.fetchActorDetails(actorId)

    override suspend fun getActorImages(actorId: Int): List<ImageDto> =
        apiService.fetchActorImages(actorId).profiles

    override suspend fun getActorMovies(actorId: Int): List<ActorCastCreditDto> =
        apiService.fetchActorMovies(actorId).cast

    override suspend fun getActorTvShows(actorId: Int): List<ActorCastCreditDto> =
        apiService.fetchActorTvShows(actorId).cast
}