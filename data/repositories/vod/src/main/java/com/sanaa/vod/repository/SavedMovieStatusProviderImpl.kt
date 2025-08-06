package com.sanaa.vod.repository

import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.custom_list.RemoteSavedListDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import repository.SavedMovieStatusProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedMovieStatusProviderImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val preferencesManager: PreferencesManager
) : SavedMovieStatusProvider {

    private val _savedIds = MutableStateFlow(emptySet<Int>())
    override val savedIds: StateFlow<Set<Int>> = _savedIds
    private val mutex = Mutex()

    override suspend fun isSaved(id: Int): Boolean {
        if (_savedIds.value.isEmpty()) refresh()
        return _savedIds.value.contains(id)
    }

    override suspend fun refresh() {
        mutex.withLock {
            if (_savedIds.value.isNotEmpty()) return
            _savedIds.value = fetchAllMoviesIds()
        }
    }

    private suspend fun fetchAllMoviesIds(): Set<Int> = coroutineScope {
        val sessionId = preferencesManager.sessionId.first()
        remoteSavedListDataSource
            .fetchUserLists(sessionId)
            .map { list ->
                async {
                    remoteSavedListDataSource
                        .fetchListItems(list.id, null)
                        .map { it.id }
                }
            }
            .awaitAll()
            .flatten()
            .toSet()
    }

    override fun markSaved(id: Int)   = _savedIds.update { it + id }
    override fun markUnsaved(id: Int) = _savedIds.update { it - id }
}