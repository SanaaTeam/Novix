package com.sanaa.vod.dataSource.remote

import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorCastCreditDto
import com.sanaa.vod.dataSource.remote.dto.actor.ActorDto

interface RemoteActorDataSource {
    suspend fun getActorDetails(actorId: Int): ActorDto
    suspend fun getActorImages(actorId: Int): List<ImageDto>
    suspend fun getActorMovies(actorId: Int): List<ActorCastCreditDto>
    suspend fun getActorTvShows(actorId: Int): List<ActorCastCreditDto>
    suspend fun fetchTrendingPeople(page: Int): List<ActorDto>
}