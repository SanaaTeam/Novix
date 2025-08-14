package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.local.cache.LocalSavedListDataSource
import com.sanaa.vod.dataSource.remote.RemoteMovieDataSource
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import com.sanaa.vod.repository.mapper.media.toEntity
import com.sanaa.vod.util.safeCall
import entity.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedListRepositoryImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val remoteMovieDataSource: RemoteMovieDataSource,
    private val localSavedListDataSource: LocalSavedListDataSource,
    private val preferences: PreferencesManager
) : SavedListRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeSavedLists(): Flow<List<SavedList>> =
        localSavedListDataSource.observeLists().flatMapLatest { lists ->
            if (lists.isEmpty()) {
                flow {
                    val sessionId = preferences.sessionId.first()
                    val remoteLists = remoteSavedListDataSource.fetchUserLists(sessionId)
                    localSavedListDataSource.upsertLists(remoteLists.map {
                        SavedList(
                            it.id,
                            it.title,
                            it.itemCount
                        )
                    })
                    emit(remoteLists.map { SavedList(it.id, it.title, it.itemCount) })
                }
            } else {
                flow { emit(lists) }
            }
        }

    override suspend fun getSavedListsOnce(): List<SavedList> {
        val localLists = localSavedListDataSource.getListsOnce()
        return if (localLists.isEmpty()) {
            val sessionId = preferences.sessionId.first()
            val remoteLists = remoteSavedListDataSource.fetchUserLists(sessionId)
            localSavedListDataSource.upsertLists(remoteLists.map {
                SavedList(
                    it.id,
                    it.title,
                    it.itemCount
                )
            })
            remoteLists.map { SavedList(it.id, it.title, it.itemCount) }
        } else {
            localLists
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeMoviesInList(listId: Int): Flow<List<Movie>> =
        localSavedListDataSource.observeMovieIdsForList(listId)
            .flatMapLatest { ids ->
                flow {
                    val movies = coroutineScope {
                        ids.map { id ->
                            async {
                                remoteMovieDataSource.fetchMovieDetails(id)
                                    .toEntity()
                                    .copy(isSaved = true)
                            }
                        }.awaitAll()
                    }
                    emit(movies)
                }
            }

    override suspend fun isMovieSaved(movieId: Int): Boolean =
        localSavedListDataSource.isMovieSaved(movieId)

    override suspend fun createSavedList(title: String): SavedList =
        safeCall("Failed to create list") {
            val sessionId = preferences.sessionId.first()
            val created = remoteSavedListDataSource.createList(sessionId, title)
            val localList = SavedList(created.id, created.title, created.itemCount)
            localSavedListDataSource.upsertList(localList)
            localList
        }

    override suspend fun deleteSavedList(listId: Int) =
        safeCall("Failed to delete list") {
            val sessionId = preferences.sessionId.first()
            remoteSavedListDataSource.deleteList(sessionId, listId)
            localSavedListDataSource.deleteList(listId)
        }

    override suspend fun addMovieToList(listId: Int, movieId: Int) {
        safeCall("Failed to add movie to list") {
            localSavedListDataSource.addItem(listId, movieId)
            val sessionId = preferences.sessionId.first()
            remoteSavedListDataSource.addItem(sessionId, listId, movieId)
        }
    }

    override suspend fun removeMovieFromList(listId: Int, movieId: Int) {
        safeCall("Failed to remove movie from list") {
            localSavedListDataSource.removeItem(listId, movieId)
            val sessionId = preferences.sessionId.first()
            remoteSavedListDataSource.removeItem(sessionId, listId, movieId)
        }
    }

    override suspend fun getMoviesInList(listId: Int, page: Int): List<Movie> =
        safeCall("Failed to load movies in list") {
            val ids = localSavedListDataSource.getMovieIdsForListOnce(listId)
            val pageSize = 20
            val start = (page - 1).coerceAtLeast(0) * pageSize
            val slice = ids.drop(start).take(pageSize)
            if (slice.isEmpty()) return@safeCall emptyList()

            coroutineScope {
                slice.map { id ->
                    async {
                        remoteMovieDataSource.fetchMovieDetails(id).toEntity()
                            .copy(isSaved = true)
                    }
                }.awaitAll()
            }
        }
}