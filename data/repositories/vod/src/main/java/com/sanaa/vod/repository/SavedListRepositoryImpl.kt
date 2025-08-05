package com.sanaa.vod.repository

import android.icu.text.CaseMap
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.mapper.custom_list.toDomain
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.flow.first
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.*
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remote: RemoteSavedListDataSource,
    private val prefs: PreferencesManager
) : SavedListRepository {

    private suspend fun sessionId(): String = prefs.sessionId.first()

    override suspend fun getSavedLists(accountId: Long): List<SavedList> =
        safeCall("Failed to fetch saved lists") {
            remote.fetchUserLists(sessionId(), accountId).map { it.toDomain() }
        }

    override suspend fun createSavedList(title: String): SavedList =
        safeCall("Failed to create list") {
            remote.createList(sessionId(), title).toDomain()
        }

    override suspend fun deleteSavedList(listId: Int) {
        safeCall("Failed to delete list") {
            remote.deleteList(sessionId(), listId)
        }
    }

    override suspend fun getAllMoviesInList(listId: Int): List<Movie> =
        safeCall("Failed to fetch list items") {
            remote.fetchListItems(listId).map { it.toDomain() }
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to add movie to list") {
            remote.addItem(sessionId(), listId, movieId)
        }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean =
        safeCall("Failed to remove item from list") {
            remote.removeItem(sessionId(), listId, movieId)
        }
}