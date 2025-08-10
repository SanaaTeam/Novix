package repository

import kotlinx.coroutines.flow.StateFlow
import usecase.custom_list.custom_list_param.SavedList

interface SavedListsStatusProvider {
    val savedIds: StateFlow<Set<Int>>
    val savedLists: StateFlow<List<SavedList>>
    suspend fun refreshItems()
    suspend fun isItemSaved(id: Int): Boolean
    fun markItemSaved(id: Int)
    fun markItemUnsaved(id: Int)
    suspend fun refreshLists()
    fun addList(list: SavedList)
}
