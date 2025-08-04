package repository

import usecase.custom_list.custom_list_param.SavedItem
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(accountId: Long): List<SavedList>
    suspend fun createSavedList(list: SavedList): SavedList
    suspend fun deleteSavedList(listId: Int)

    suspend fun getAllItemsInList(listId: Int): List<SavedItem>
    suspend fun addMovieToList(listId: Int, movieId: Int): Boolean
    suspend fun removeMovieFromList(listId: Int, movieId: Int): Boolean
}