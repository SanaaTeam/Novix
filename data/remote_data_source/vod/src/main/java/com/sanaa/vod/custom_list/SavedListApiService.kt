package com.sanaa.vod.custom_list

import com.sanaa.vod.custom_list.request.AddItemBodyDto
import com.sanaa.vod.custom_list.request.CreateListBodyDto
import com.sanaa.vod.custom_list.request.RemoveItemBodyDto
import com.sanaa.vod.custom_list.request.TmdbStatusDto
import com.sanaa.vod.custom_list.response.CreateListResponseDto
import com.sanaa.vod.custom_list.response.ListApiResponse
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDetailsDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SavedListApiService {

    /* ---------- account lists ---------- */
    @GET("account/{account_id}/lists")
    suspend fun getUserLists(
        @Path("account_id") accountId: Long,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int? = null
    ): ListApiResponse<SavedListDto>

    /* ---------- list CRUD ---------- */
    @Headers("Ignore-Language: true")
    @POST("list")
    suspend fun createList(
        @Query("session_id") sessionId: String,
        @Body body: CreateListBodyDto,
    ): CreateListResponseDto

    @DELETE("list/{list_id}")
    suspend fun deleteList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String
    ): TmdbStatusDto

    /* ---------- list details ---------- */
    @GET("list/{list_id}")
    suspend fun getListDetails(
        @Path("list_id") listId: Int,
        @Query("page") page: Int? = null
    ): SavedListDetailsDto

    /* ---------- items ---------- */
    @Headers("Ignore-Language: true")
    @POST("list/{list_id}/add_item")
    suspend fun addItem(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Body body: AddItemBodyDto
    ): TmdbStatusDto

    @POST("list/{list_id}/remove_item")
    @Headers("Ignore-Language: true")
    suspend fun removeItem(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Body body: RemoveItemBodyDto
    ): TmdbStatusDto
}