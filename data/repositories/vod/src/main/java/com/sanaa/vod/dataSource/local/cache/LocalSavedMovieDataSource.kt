package com.sanaa.vod.dataSource.local.cache

import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.SavedMovieLocalDto
import kotlinx.coroutines.flow.Flow


interface LocalSavedMovieDataSource {
    suspend fun insertList(savedLis: SavedListLocalDto)
    suspend fun deleteList(listId: Int): Flow<Boolean>
    suspend fun getAllLists(): Flow<List<SavedListLocalDto>>
    suspend fun getListSavedMovies(listId: Int): Flow<List<SavedMovieLocalDto>>
    suspend fun addMovieToList(listId: Int, movieId: Int): Flow<Boolean>
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Flow<Boolean>
}