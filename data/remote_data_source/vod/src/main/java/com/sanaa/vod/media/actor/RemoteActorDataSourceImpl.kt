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

    override suspend fun getActorTopMovies(actorId: Int): List<ActorCastCreditDto> =
        apiService.fetchActorTopMovies(actorId).cast

    override suspend fun getActorTopTvSeries(actorId: Int): List<ActorCastCreditDto> =
        apiService.fetchActorTopTvSeries(actorId).cast
}