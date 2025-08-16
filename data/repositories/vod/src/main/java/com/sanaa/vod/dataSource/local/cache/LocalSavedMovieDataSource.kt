package com.sanaa.vod.dataSource.local.cache

import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import kotlinx.coroutines.flow.Flow


interface LocalSavedMovieDataSource {
    suspend fun insertList(savedLis: SavedListLocalDto)
    suspend fun deleteList(listId: Int)
    suspend fun getAllLists(): Flow<List<SavedListLocalDto>>
    suspend fun addMovieToList(listId: Int, movieId: Int)
    suspend fun removeMovieFromList(listId: Int, movieId: Int)
}