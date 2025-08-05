package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.flow.first
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.*
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val preferencesManager: PreferencesManager
) : SavedListRepository {

    private suspend fun sessionId(): String = preferencesManager.sessionId.first()

    override suspend fun getSavedLists(accountId: Long): List<SavedList> =
        safeCall("Failed to fetch saved lists") {
            remoteSavedListDataSource.fetchUserLists(sessionId(), accountId).map { it.toEntity() }
        }

    override suspend fun createSavedList(title: String): SavedList =
        safeCall("Failed to create list") {
            remoteSavedListDataSource.createList(sessionId(), title).toEntity()
        }

    override suspend fun deleteSavedList(listId: Int) {
        safeCall("Failed to delete list") {
            remoteSavedListDataSource.deleteList(sessionId(), listId)
        }
    }

    override suspend fun getAllMoviesInList(listId: Int): List<Movie> =
        safeCall("Failed to fetch list items") {
            remoteSavedListDataSource.fetchListItems(listId).map {
                remoteMovieDataSource.fetchMovieDetails(
                    it.toEntity().id
                ).toEntity()
            }
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to add movie to list") {
            remoteSavedListDataSource.addItem(sessionId(), listId, movieId)
        }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to remove item from list") {
            remoteSavedListDataSource.removeItem(sessionId(), listId, movieId)
        }
}