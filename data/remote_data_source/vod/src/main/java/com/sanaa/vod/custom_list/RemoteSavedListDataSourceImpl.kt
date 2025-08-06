package com.sanaa.vod.custom_list

import com.sanaa.vod.custom_list.request.AddOrRemoveItemBody
import com.sanaa.vod.custom_list.request.CreateListBody
import com.sanaa.vod.custom_list.response.TmdbStatusResponseDto
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto
import javax.inject.Inject

class RemoteSavedListDataSourceImpl @Inject constructor(
    private val savedListApiService: SavedListApiService
) : RemoteSavedListDataSource {

    override suspend fun fetchUserLists(sessionId: String, page: Int?) =
        savedListApiService.getUserLists(sessionId = sessionId, page = page).results

    override suspend fun createList(
        sessionId: String,
        name: String,
        description: String
    ): SavedListDto {
        val response = savedListApiService
            .createList(sessionId, CreateListBody(name, description, DEFAULT_LANGUAGE))
        return savedListApiService.getListDetails(response.listId).toListDto()
    }

    override suspend fun deleteList(sessionId: String, listId: Int) {
        savedListApiService.deleteList(listId, sessionId)
    }

    override suspend fun fetchListItems(listId: Int, page: Int?) =
        savedListApiService.getListDetails(listId, page).items

    override suspend fun addItem(
        sessionId: String,
        listId: Int,
        movieId: Int
    ): Boolean {
        val body = AddOrRemoveItemBody(movieId)
        return attemptAddItem(sessionId, listId, body, MAX_RETRIES).success
    }

    override suspend fun removeItem(sessionId: String, listId: Int, movieId: Int) =
        savedListApiService.removeItem(listId, sessionId, AddOrRemoveItemBody(movieId)).success

    override suspend fun isItemSaved(listId: Int, movieId: Int, sessionId: String): Boolean =
        savedListApiService
            .checkItemStatus(listId, movieId, sessionId)
            .itemPresent

    private tailrec suspend fun attemptAddItem(
        sessionId: String,
        listId: Int,
        body: AddOrRemoveItemBody,
        remainingAttempts: Int
    ): TmdbStatusResponseDto {
        val response = savedListApiService.addItem(listId, sessionId, body)

        return when {
            response.statusCode != ENTRY_NOT_FOUND_CODE -> response
            remainingAttempts <= 1 -> response
            else -> attemptAddItem(sessionId, listId, body, remainingAttempts - 1)
        }
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
        private const val ENTRY_NOT_FOUND_CODE = 21
        private const val MAX_RETRIES = 10
    }
}