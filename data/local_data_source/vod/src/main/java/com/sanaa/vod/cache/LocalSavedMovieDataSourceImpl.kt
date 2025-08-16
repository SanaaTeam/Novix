package com.sanaa.vod.cache

import com.sanaa.vod.cache.dao.SavedListDao
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalSavedMovieDataSourceImpl @Inject constructor(
    private val dao: SavedListDao,
) : LocalSavedMovieDataSource {

    override suspend fun insertList(savedList: SavedListLocalDto) {
        dao.insertList(savedList)
    }

    override suspend fun deleteList(listId: Int) {
        dao.deleteList(listId)
    }

    override suspend fun getAllLists(): Flow<List<SavedListLocalDto>> {
        return dao.getAllLists()
    }

    override suspend fun addMovieToList(listId: Int, movieId: Int) {
        val list = dao.getListById(listId) ?: return

        val ids = list.movieIds.toIdList().toMutableSet()

        val changed = ids.add(movieId)

        if (changed) {
            dao.insertList(list.copy(movieIds = ids.toMutableList().toIdString()))
        }
    }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int) {
        val list = dao.getListById(listId) ?: return

        val ids = list.movieIds.toIdList().toMutableSet()

        val changed = ids.remove(movieId)

        if (changed) {
            dao.insertList(list.copy(movieIds = ids.toMutableList().toIdString()))
        }
    }

    private fun String.toIdList(): List<Int> =
        split(",").mapNotNull { it.toIntOrNull() }.filter { it > 0 }

    private fun List<Int>.toIdString(): String =
        joinToString(",")
}