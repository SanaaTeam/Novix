package com.sanaa.vod.cache

import com.sanaa.vod.cache.dao.SavedListDao
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.local.cache.dto.SavedListLocalDto
import com.sanaa.vod.dataSource.local.cache.dto.SavedMovieLocalDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalSavedMovieDataSourceImpl @Inject constructor(
    private val dao: SavedListDao,
) : LocalSavedMovieDataSource {

    override suspend fun insertList(savedLis: SavedListLocalDto) {
        dao.insertList(savedLis)
    }

    override suspend fun deleteList(listId: Int): Flow<Boolean> =
        flow { emit(dao.deleteList(listId) > 0) }

    override suspend fun getAllLists(): Flow<List<SavedListLocalDto>> {
        return dao.getAllLists()
    }

    override suspend fun getListSavedMovies(listId: Int): Flow<List<SavedMovieLocalDto>> =
        dao.getListById(listId)
            .filterNotNull()
            .map { list ->
                val ids = list.movieIds.toIdList()
                if (ids.isEmpty()) emptyList()
                else dao.getMoviesByIds(ids).firstOrNull().orEmpty()
            }

    override suspend fun addMovieToList(
        listId: Int,
        movieId: Int
    ): Flow<Boolean> = updateListMovies(listId, movieId, add = true)

    override suspend fun removeMovieFromList(
        listId: Int,
        movieId: Int
    ): Flow<Boolean> = updateListMovies(listId, movieId, add = false)


    private  fun updateListMovies(
        listId: Int,
        movieId: Int,
        add: Boolean
    ): Flow<Boolean> = flow {
        val list = dao.getListById(listId).firstOrNull()
            ?: return@flow emit(false)

        val ids = list.movieIds.toIdList().toMutableList()

        val changed = if (add) {
            if (!ids.contains(movieId)) {
                ids.add(movieId)
                true
            } else false
        } else {
            ids.remove(movieId)
        }

        if (changed) {
            dao.insertList(list.copy(movieIds = ids.toIdString()))
        }
        emit(changed)
    }


    private fun String.toIdList(): List<Int> =
        split(",").mapNotNull { it.toIntOrNull() }.filter { it > 0 }

    private fun List<Int>.toIdString(): String =
        joinToString(",")
}