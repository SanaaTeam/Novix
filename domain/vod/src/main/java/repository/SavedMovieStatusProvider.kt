package repository

import kotlinx.coroutines.flow.StateFlow

interface SavedMovieStatusProvider {
    val savedIds: StateFlow<Set<Int>>
    suspend fun refresh()
    suspend fun isSaved(id: Int): Boolean
    fun markSaved(id: Int)
    fun markUnsaved(id: Int)
}