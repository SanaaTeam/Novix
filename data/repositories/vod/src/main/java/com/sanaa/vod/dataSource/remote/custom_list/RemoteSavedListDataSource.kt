package com.sanaa.vod.dataSource.remote.custom_list

import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemRemoteDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto

interface RemoteSavedListDataSource {
    suspend fun fetchUserLists(sessionId: String, page: Int? = null): List<SavedListRemoteDto>
    suspend fun createList(sessionId: String, name: String, description: String = ""): SavedListRemoteDto
    suspend fun deleteList(sessionId: String, listId: Int): Boolean
    suspend fun fetchListItems(listId: Int, page: Int? = null): List<SavedItemRemoteDto>
    suspend fun addItem(sessionId: String, listId: Int, movieId: Int): Boolean
    suspend fun removeItem(sessionId: String, listId: Int, movieId: Int): Boolean
}