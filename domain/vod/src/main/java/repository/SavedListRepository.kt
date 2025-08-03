package repository

import entity.Movie
import entity.TvSeries
import usecase.custom_list.custom_list_param.SavedItem
import usecase.custom_list.custom_list_param.SavedList

interface SavedListRepository {
    suspend fun getSavedLists(): List<SavedList>
    suspend fun createSavedList(list: SavedList): SavedList
    suspend fun editSavedList(list: SavedList)
    suspend fun deleteSavedList(listId: Int)

    suspend fun getAllItemsInList(listId: Int): List<SavedItem>
    suspend fun getMoviesInList(listId: Int): List<Movie>
    suspend fun getTvSeriesInList(listId: Int): List<TvSeries>

    suspend fun addMovieToList(listId: Int, movieId: Int)
    suspend fun addTvSeriesToList(listId: Int, tvSeriesId: Int)
    suspend fun removeItemFromList(itemId: Int)
}