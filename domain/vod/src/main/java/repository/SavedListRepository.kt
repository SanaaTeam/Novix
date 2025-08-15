package repository

import entity.Movie
import kotlinx.coroutines.flow.Flow
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(): Flow<List<SavedList>>
    suspend fun createSavedList(title: String): Flow<SavedList>
    suspend fun deleteSavedList(listId: Int):Flow<Boolean>

    suspend fun getAllMoviesInList(listId: Int, page: Int): Flow<List<Movie>>
    suspend fun addMovieToList(listId: Int, movieId: Int): Flow<Boolean>
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Flow<Boolean>
}