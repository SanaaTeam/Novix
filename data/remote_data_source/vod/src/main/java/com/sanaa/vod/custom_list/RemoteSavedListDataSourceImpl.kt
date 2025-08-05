package com.sanaa.vod.custom_list

import com.sanaa.vod.custom_list.request.AddItemBodyDto
import com.sanaa.vod.custom_list.request.CreateListBodyDto
import com.sanaa.vod.custom_list.request.RemoveItemBodyDto
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto

import javax.inject.Inject

class RemoteSavedListDataSourceImpl @Inject constructor(
    private val api: SavedListApiService
) : RemoteSavedListDataSource {

    override suspend fun fetchUserLists(sessionId: String, accountId: Long) =
        api.getUserLists(accountId, sessionId).results

    override suspend fun createList(
        sessionId: String,
        name: String,
        description: String
    ): SavedListDto {
        val res = api.createList(sessionId, CreateListBodyDto(name, description, "en"))
        // Pull full object so we have counts & description
        return api.getListDetails(res.listId).toListDto()
    }

    override suspend fun deleteList(sessionId: String, listId: Int) {
        api.deleteList(listId, sessionId)
    }

    override suspend fun fetchListItems(listId: Int, page: Int?) =
        api.getListDetails(listId, page).items

    override suspend fun addItem(sessionId: String, listId: Int, movieId: Int) =
        api.addItem(listId, sessionId, AddItemBodyDto(mediaId = movieId)).success

    override suspend fun removeItem(sessionId: String, listId: Int, movieId: Int) =
        api.removeItem(listId, sessionId, RemoveItemBodyDto(movieId)).success
}