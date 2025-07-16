package com.sanaa.actors.dataSource.remote

import com.sanaa.actors.dataSource.remote.dto.ActorDto
import com.sanaa.actors.dataSource.remote.dto.ActorImagesDto
import com.sanaa.actors.dataSource.remote.dto.ActorMovieCastDto
import com.sanaa.actors.dataSource.remote.dto.ActorTvCastDto

interface ActorRemoteDataSource {

    suspend fun getActorDetails(
        actorId: Int
    ): ActorDto

    suspend fun getActorImages(
        actorId: Int
    ): ActorImagesDto

    suspend fun getActorTopMovies(
        actorId: Int
    ): ActorMovieCastDto

    suspend fun getActorTopTvSeries(
        actorId: Int
    ): ActorTvCastDto
}
