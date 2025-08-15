package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedMovieDataSource
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.custom_list.toEntity
import com.sanaa.vod.repository.mapper.custom_list.toLocalDto
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val localSavedMovieDataSource: LocalSavedMovieDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
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
                            itemCount = it.movieIds.split(",").filter { id -> id.isNotBlank() }.size,
                            itemsIds = it.movieIds.split(",").filter { id -> id.isNotBlank() }.map { id -> id.toInt() }
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

    override suspend fun createSavedList(title: String): Flow<SavedList> =
        safeCall("Failed to create list") {
            val createdList = remoteSavedListDataSource.createList(obtainSessionId(), title)
            localSavedMovieDataSource.insertList(
                createdList.toLocalDto(
                    movieIds = ""
                )
            )
            flow {
                emit(createdList.toEntity())
            }
        }

    override suspend fun deleteSavedList(listId: Int): Flow<Boolean> =
        safeCall("Failed to delete list") {
            flow {
                remoteSavedListDataSource.deleteList(obtainSessionId(), listId)
                localSavedMovieDataSource.deleteList(listId)
                true
            }
        }

    override suspend fun getAllMoviesInList(listId: Int, page: Int): Flow<List<Movie>> =
        safeCall("Failed to fetch list items") {

            flow {
                val detailedMovies = coroutineScope {
                    remoteSavedListDataSource.fetchListItems(listId, page)
                        .map { listItem ->
                            async {
                                remoteMovieDataSource.fetchMovieDetails(listItem.id).toEntity()
                            }
                        }.awaitAll()
                }
                detailedMovies.forEach {
                    localSavedMovieDataSource.addMovieToList(listId, it.id)
                }

                emit(detailedMovies)
            }
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int): Flow<Boolean> =
        safeCall("Failed to add movie to list") {
            flow {
                val success = remoteSavedListDataSource.addItem(obtainSessionId(), listId, movieId)
                if (success) {
                    localSavedMovieDataSource.addMovieToList(listId, movieId)
                }
                success
            }
        }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int): Flow<Boolean> =
        safeCall("Failed to remove movie from list") {
            flow {
                val success =
                    remoteSavedListDataSource.removeItem(obtainSessionId(), listId, movieId)
                if (success) {
                    localSavedMovieDataSource.removeMovieFromList(listId, movieId)
                }
                success
            }
        }

    private suspend fun obtainSessionId(): String =
        preferencesManager.sessionId.first()
}