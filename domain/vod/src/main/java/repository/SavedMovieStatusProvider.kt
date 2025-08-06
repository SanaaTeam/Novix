package repository

import kotlinx.coroutines.flow.StateFlow

interface SavedMovieStatusProvider {
    val savedIds: StateFlow<Set<Int>>
    suspend fun refresh()
    suspend fun isSaved(id: Int): Boolean
}