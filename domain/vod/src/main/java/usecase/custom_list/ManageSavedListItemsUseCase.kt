package usecase.custom_list

import entity.Movie
import entity.TvSeries
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedItem
import javax.inject.Inject

class ManageSavedListItemsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getAllItemsInSavedList(listId: Int): List<SavedItem> =
        savedListRepository.getAllItemsInList(listId)

    suspend fun getMoviesInSavedList(listId: Int): List<Movie> =
        savedListRepository.getMoviesInList(listId)

    suspend fun getTvSeriesInSavedList(listId: Int): List<TvSeries> =
        savedListRepository.getTvSeriesInList(listId)

    suspend fun addMovieToSavedList(listId: Int, movieId: Int) =
        savedListRepository.addMovieToList(listId, movieId)

    suspend fun addTvSeriesToSavedList(listId: Int, tvSeriesId: Int) =
        savedListRepository.addTvSeriesToList(listId, tvSeriesId)

    suspend fun removeItemFromSavedList(itemId: Int) =
        savedListRepository.removeItemFromList(itemId)
}