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
import repository.SavedListsStatusProvider
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedListsStatusProviderImpl @Inject constructor(
    private val remoteSavedListDataSource: RemoteSavedListDataSource,
    private val preferencesManager: PreferencesManager
) : SavedListsStatusProvider {

    private val _savedIds = MutableStateFlow(emptySet<Int>())
    override val savedIds: StateFlow<Set<Int>> = _savedIds
    private val idsMutex = Mutex()

    private val _savedLists = MutableStateFlow<List<SavedList>>(emptyList())
    override val savedLists: StateFlow<List<SavedList>> = _savedLists
    private val listsMutex = Mutex()

    override suspend fun isItemSaved(id: Int): Boolean {
        if (_savedIds.value.isEmpty()) refreshItems()
        return _savedIds.value.contains(id)
    }

    override suspend fun refreshItems() {
        idsMutex.withLock {
            if (_savedIds.value.isNotEmpty()) return
            _savedIds.value = fetchAllMoviesIds()
        }
    }

    override suspend fun refreshLists() {
        listsMutex.withLock {
            val sessionId = preferencesManager.sessionId.first()
            val lists = remoteSavedListDataSource.fetchUserLists(sessionId)
            _savedLists.value = lists.map {
                SavedList(
                    id = it.id,
                    title = it.title,
                    itemCount = it.itemCount,
                    itemsIds = emptyList()
                )
            }
        }
    }

    override fun addList(list: SavedList) {
        _savedLists.update { current ->
            if (current.any { it.id == list.id }) current else current + list
        }
    }

    override fun markItemSaved(id: Int) = _savedIds.update { it + id }
    override fun markItemUnsaved(id: Int) = _savedIds.update { it - id }

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
}