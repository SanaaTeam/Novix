package com.sanaa.vod.cache

import com.sanaa.vod.cache.dao.SavedListDao
import com.sanaa.vod.cache.dao.SavedListItemDao
import com.sanaa.vod.dataSource.local.cache.LocalSavedListDataSource
import com.sanaa.vod.dataSource.local.cache.dto.SavedListItemLocalDto
import com.sanaa.vod.repository.mapper.custom_list.toLocalDto
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class LocalSavedListDataSourceImpl @Inject constructor(
    private val savedListDao: SavedListDao,
    private val savedListItemDao: SavedListItemDao
) : LocalSavedListDataSource {

    override fun observeLists(): Flow<List<SavedList>> =
        savedListDao.observeAll().map { it.map { dto -> dto.toEntity() } }

    override suspend fun getListsOnce(): List<SavedList> =
        savedListDao.getAllOnce().map { it.toEntity() }

    override suspend fun upsertLists(lists: List<SavedList>) {
        savedListDao.upsertAll(lists.map { it.toLocalDto() })
    }

    override suspend fun upsertList(list: SavedList) {
        savedListDao.upsert(list.toLocalDto())
    }

    override suspend fun deleteList(listId: Int) {
        savedListItemDao.deleteByList(listId)
        savedListDao.deleteById(listId)
    }


    override suspend fun clearLists() = savedListDao.clear()

    override fun observeMovieIdsForList(listId: Int): Flow<List<Int>> =
        savedListItemDao.observeMovieIdsForList(listId)

    override suspend fun getMovieIdsForListOnce(listId: Int): List<Int> =
        savedListItemDao.getMovieIdsForListOnce(listId)

    override suspend fun addItem(listId: Int, movieId: Int) {
        savedListItemDao.upsert(SavedListItemLocalDto(listId, movieId))
    }

    override suspend fun removeItem(listId: Int, movieId: Int) {
        savedListItemDao.delete(listId, movieId)
    }

    override suspend fun clearAllItems() = savedListItemDao.clear()

    override suspend fun isMovieSaved(movieId: Int): Boolean = savedListItemDao.isMovieSaved(movieId)

    override fun observeAllSavedMovieIds(): Flow<List<Int>> = savedListItemDao.observeAllSavedMovieIds()
}