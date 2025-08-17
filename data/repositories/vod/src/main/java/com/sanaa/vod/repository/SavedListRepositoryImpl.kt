package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.dataSource.remote.dto.cutsom_list.SavedListRemoteDto
import com.sanaa.vod.repository.mapper.savedList.toEntity
import com.sanaa.vod.repository.mapper.savedList.toLocalDto
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

    override suspend fun getLocalSavedLists(): Flow<List<SavedList>> =
        safeCall("Failed to get local saved lists") {
            localSavedMovieDataSource.getAllLists().map {
                it.toEntity()
            }
        }

    private suspend fun fetchRemoteSaveLists(): List<SavedList> =
        safeCall("failed to fetch remote saved lists ") {
            val remoteLists = remoteSavedListDataSource.fetchUserLists(obtainSessionId())
            val itemsIds = fetchRemoteListsItems(remoteLists)
            remoteLists.toEntity(itemsIds)
        }

    private suspend fun fetchRemoteListsItems(remoteLists: List<SavedListRemoteDto>) :List<Int>{
        var movieIds = emptyList<Int>()
        remoteLists.forEach { remoteList ->
             movieIds = remoteSavedListDataSource
                .fetchListItems(remoteList.id)
                .map { it.id }
        }
        return movieIds

    }


    override suspend fun refreshSavedLists() {
        val remoteLists = fetchRemoteSaveLists()
        clearAllLists()
        insertLocalLists(remoteLists)
    }

    private suspend fun insertLocalLists(lists: List<SavedList>) {
        safeCall("failed to insert remote lists to local database") {
            lists.forEach {
                localSavedMovieDataSource.insertList(it.toLocalDto())
            }
        }
    }


    override suspend fun createSavedList(title: String) {
        safeCall("Failed to create list") {
            val createdList = remoteSavedListDataSource.createList(obtainSessionId(), title)
            localSavedMovieDataSource.insertList(
                createdList.toEntity(emptyList()).toLocalDto()
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

    override suspend fun clearAllLists() {
        safeCall("error clearing all lists data from database") {
            localSavedMovieDataSource.clearAllLists()
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