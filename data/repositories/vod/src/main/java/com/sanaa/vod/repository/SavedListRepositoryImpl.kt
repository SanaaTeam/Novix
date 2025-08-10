package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import repository.SavedListRepository
import repository.SavedListsStatusProvider
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val preferencesManager: PreferencesManager,
    private val savedListsStatusProvider: SavedListsStatusProvider
) : SavedListRepository {

    override suspend fun getSavedLists(): List<SavedList> =
        safeCall("Failed to fetch saved lists") {
            remoteSavedListDataSource
                .fetchUserLists(obtainSessionId())
                .map { it.toEntity() }
        }

    override suspend fun createSavedList(title: String): SavedList =
        safeCall("Failed to create list") {
            remoteSavedListDataSource
                .createList(obtainSessionId(), title)
                .toEntity()
        }

    override suspend fun deleteSavedList(listId: Int) {
        safeCall("Failed to delete list") {
            remoteSavedListDataSource.deleteList(obtainSessionId(), listId)
            savedListsStatusProvider.refreshItems()
        }
    }

    override suspend fun getAllMoviesInList(listId: Int, page: Int): List<Movie> =
        safeCall("Failed to fetch list items") {
            val listItems = remoteSavedListDataSource.fetchListItems(listId, page)
            coroutineScope {
                listItems.map { listItem ->
                    async {
                        val movie = remoteMovieDataSource.fetchMovieDetails(listItem.id).toEntity()
                        movie.copy(isSaved = savedListsStatusProvider.isItemSaved(movie.id))
                    }
                }.awaitAll()
            }
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to add movie to list") {
            remoteSavedListDataSource
                .addItem(obtainSessionId(), listId, movieId)
                .also { success -> if (success) savedListsStatusProvider.refreshItems() }
        }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to remove movie from list") {
            remoteSavedListDataSource
                .removeItem(obtainSessionId(), listId, movieId)
                .also { success -> if (success) savedListsStatusProvider.refreshItems() }
        }

    private suspend fun obtainSessionId(): String = preferencesManager.sessionId.first()
}