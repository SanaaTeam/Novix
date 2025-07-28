package com.sanaa.vod.dataSource.remote.actor

import com.sanaa.vod.dataSource.remote.dto.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto

interface RemoteActorDataSource {

    suspend fun getActorDetails(actorId: Int): ActorDto

    suspend fun getActorImages(actorId: Int): List<ImageDto>

    suspend fun getActorMovies(actorId: Int): List<ActorCastCreditDto>

    suspend fun getActorTvShows(actorId: Int): List<ActorCastCreditDto>

    suspend fun fetchTrendingPeople(page: Int): List<ActorDto>
}