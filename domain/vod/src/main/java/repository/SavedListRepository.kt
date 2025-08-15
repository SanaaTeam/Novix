package repository

import entity.Movie
import kotlinx.coroutines.flow.Flow
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(): Flow<List<SavedList>>
    suspend fun createSavedList(title: String): Boolean
    suspend fun deleteSavedList(listId: Int): Boolean

    suspend fun getAllMoviesInList(listId: Int): Flow<List<Movie>>
    suspend fun addMovieToList(listId: Int, movieId: Int): Boolean
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean
}