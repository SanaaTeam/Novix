package repository

import entity.Movie
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(accountId: Long): List<SavedList>
    suspend fun createSavedList(list: SavedList): SavedList
    suspend fun deleteSavedList(listId: Int)

    suspend fun getAllMoviesInList(listId: Int): List<Movie>
    suspend fun addMovieToList(listId: Int, movieId: Int): Boolean
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean
}