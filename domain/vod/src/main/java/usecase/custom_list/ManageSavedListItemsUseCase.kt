package usecase.custom_list

import entity.Movie
import repository.SavedListRepository
import javax.inject.Inject

class ManageSavedListItemsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getAllItemsInSavedList(listId: Int, page: Int): List<Movie> =
        savedListRepository.getAllMoviesInList(listId, page)

    suspend fun addMovieToSavedList(listId: Int, movieId: Int): Boolean =
        savedListRepository.addMovieToList(listId = listId, movieId = movieId)

    suspend fun removeMovieFromSavedList(listId: Int, movieId: Int): Boolean =
        savedListRepository.removeMovieFromList(listId, movieId)
}