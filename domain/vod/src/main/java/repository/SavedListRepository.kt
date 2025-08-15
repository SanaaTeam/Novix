package repository

import entity.Movie
import kotlinx.coroutines.flow.Flow
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(): Flow<List<SavedList>>
    suspend fun createSavedList(title: String)
    suspend fun deleteSavedList(listId: Int)

    suspend fun getMoviesInList(listId: Int, page: Int): List<Movie>
    suspend fun addMovieToList(listId: Int, movieId: Int)
    suspend fun removeMovieFromList(listId: Int, movieId: Int)
}