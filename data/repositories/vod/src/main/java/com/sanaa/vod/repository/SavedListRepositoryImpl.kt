package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import com.sanaa.vod.repository.mapper.custom_list.toLocalDto
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val localSavedMovieDataSource: LocalSavedMovieDataSource,
    private val preferencesManager: PreferencesManager,
) : SavedListRepository {

    override suspend fun getSavedLists(): Flow<List<SavedList>> =
        safeCall("Failed to fetch saved lists") {
            localSavedMovieDataSource.getAllLists()
                .map { lists ->
                    lists.map {
                        SavedList(
                            id = it.id,
                            title = it.listName,
                            itemCount = it.movieIds.split(",")
                                .filter { id -> id.isNotBlank() }.size,
                            itemsIds = it.movieIds.split(",").filter { id -> id.isNotBlank() }
                                .map { id -> id.toInt() }
                        )
                    }
                }
                .also {
                    val remoteLists = remoteSavedListDataSource.fetchUserLists(obtainSessionId())
                    remoteLists.forEach { remoteList ->
                        val movieIds = remoteSavedListDataSource
                            .fetchListItems(remoteList.id)
                            .joinToString(",") { item -> item.id.toString() }
                        localSavedMovieDataSource.insertList(remoteList.toLocalDto(movieIds))
                    }
                }
        }

    override suspend fun createSavedList(title: String) {
        safeCall("Failed to create list") {
            val createdList = remoteSavedListDataSource.createList(obtainSessionId(), title)
            localSavedMovieDataSource.insertList(
                createdList.toLocalDto(movieIds = "")
            )
        }
    }

    override suspend fun deleteSavedList(listId: Int) {
        safeCall("Failed to delete list") {
            remoteSavedListDataSource.deleteList(obtainSessionId(), listId).also { success ->
                if (!success) {
                    throw Exception("Failed to delete list from remote")
                }
                localSavedMovieDataSource.deleteList(listId)
            }
        }
    }

    override suspend fun getMoviesInList(listId: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch list items") {
            remoteSavedListDataSource.fetchListItems(listId, page).map { it.toEntity() }
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int) {
        safeCall("Failed to add movie to list") {
            remoteSavedListDataSource.addItem(obtainSessionId(), listId, movieId).also { success ->
                if (!success) {
                    throw Exception("Failed to add movie to list on remote")
                }
                localSavedMovieDataSource.addMovieToList(listId, movieId)
            }
        }
    }


    override suspend fun removeMovieFromList(listId: Int, movieId: Int) {
        safeCall("Failed to remove movie from list") {
            val success =
                remoteSavedListDataSource.removeItem(obtainSessionId(), listId, movieId)
            if (success) {
                localSavedMovieDataSource.removeMovieFromList(listId, movieId)
            } else {
                throw Exception("Failed to remove movie from list")
            }
        }
    }

    private suspend fun obtainSessionId(): String =
        preferencesManager.sessionId.first()
}