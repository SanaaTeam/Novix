package com.sanaa.vod.dataSource.local.cache

import kotlinx.coroutines.flow.Flow
import usecase.custom_list.custom_list_param.SavedList

interface LocalSavedListDataSource {
    fun observeLists(): Flow<List<SavedList>>
    suspend fun getListsOnce(): List<SavedList>
    suspend fun upsertLists(lists: List<SavedList>)
    suspend fun upsertList(list: SavedList)
    suspend fun deleteList(listId: Int)
    suspend fun clearLists()

    fun observeMovieIdsForList(listId: Int): Flow<List<Int>>
    suspend fun getMovieIdsForListOnce(listId: Int): List<Int>
    suspend fun addItem(listId: Int, movieId: Int)
    suspend fun removeItem(listId: Int, movieId: Int)
    suspend fun clearAllItems()

    suspend fun isMovieSaved(movieId: Int): Boolean
    fun observeAllSavedMovieIds(): Flow<List<Int>>
}