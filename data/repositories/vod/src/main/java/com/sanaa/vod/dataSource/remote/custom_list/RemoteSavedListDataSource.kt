package com.sanaa.vod.dataSource.remote.custom_list

import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedItemDto
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListDto

interface RemoteSavedListDataSource {

    suspend fun fetchUserLists(sessionId: String, accountId: Long, page: Int? = null): List<SavedListDto>

    suspend fun createList(
        sessionId: String,
        name: String,
        description: String = " "
    ): SavedListDto

    suspend fun deleteList(sessionId: String, listId: Int)

    suspend fun fetchListItems(listId: Int, page: Int? = null): List<SavedItemDto>

    suspend fun addItem(sessionId: String, listId: Int, movieId: Int): Boolean

    suspend fun removeItem(sessionId: String, listId: Int, movieId: Int): Boolean
}