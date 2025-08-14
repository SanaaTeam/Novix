package repository

import entity.Movie
import kotlinx.coroutines.flow.Flow
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    fun observeSavedLists(): Flow<List<SavedList>>
    suspend fun getSavedListsOnce(): List<SavedList>
    fun observeMoviesInList(listId: Int): Flow<List<Movie>>
    suspend fun isMovieSaved(movieId: Int): Boolean

    suspend fun createSavedList(title: String): SavedList
    suspend fun deleteSavedList(listId: Int)

    suspend fun addMovieToList(listId: Int, movieId: Int)
    suspend fun removeMovieFromList(listId: Int, movieId: Int)

    suspend fun getMoviesInList(listId: Int, page: Int): List<Movie>
}