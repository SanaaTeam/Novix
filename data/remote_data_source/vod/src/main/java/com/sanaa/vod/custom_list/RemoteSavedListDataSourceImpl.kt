package com.sanaa.vod.custom_list

import com.sanaa.vod.custom_list.request.AddOrRemoveItemBody
import com.sanaa.vod.custom_list.request.CreateListBody
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto

import javax.inject.Inject

class RemoteSavedListDataSourceImpl @Inject constructor(
    private val savedListApiService: SavedListApiService
) : RemoteSavedListDataSource {

    override suspend fun fetchUserLists(sessionId: String, accountId: Long,  page: Int?) =
        savedListApiService.getUserLists(accountId, sessionId, page).results

    override suspend fun createList(
        sessionId: String,
        name: String,
        description: String
    ): SavedListDto {
        val response = savedListApiService
            .createList(sessionId, CreateListBody(name, description, "en"))
        return savedListApiService.getListDetails(response.listId).toListDto()
    }

    override suspend fun deleteList(sessionId: String, listId: Int) {
        savedListApiService.deleteList(listId, sessionId)
    }

    override suspend fun fetchListItems(listId: Int, page: Int?) =
        savedListApiService.getListDetails(listId, page).items

    override suspend fun addItem(sessionId: String, listId: Int, movieId: Int) =
        savedListApiService.addItem(listId, sessionId, AddOrRemoveItemBody(movieId)).success

    override suspend fun removeItem(sessionId: String, listId: Int, movieId: Int) =
        savedListApiService.removeItem(listId, sessionId, AddOrRemoveItemBody(movieId)).success
}