package com.sanaa.vod.media.actor

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.media.actor.response.ActorCastCreditsResponse
import com.sanaa.vod.media.actor.response.ActorImagesResponse
import com.sanaa.vod.media.actor.response.PaginatedResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApiService {
    @GET("3/person/{actor_id}")
    suspend fun fetchActorDetails(@Path("actor_id") id: Int): ActorDto

    @GET("3/person/{actor_id}/images")
    @Headers("Ignore-Language: true")
    suspend fun fetchActorImages(@Path("actor_id") id: Int): ActorImagesResponse

    @GET("3/person/{actor_id}/movie_credits")
    suspend fun fetchActorMovies(@Path("actor_id") id: Int): ActorCastCreditsResponse

    @GET("3/person/{actor_id}/tv_credits")
    suspend fun fetchActorTvShows(@Path("actor_id") id: Int): ActorCastCreditsResponse

    @GET("3/trending/person/day")
    @Headers("Ignore-Language: true")
    suspend fun fetchTrendingPeople(@Query("page") page: Int): PaginatedResponse<ActorDto>

}