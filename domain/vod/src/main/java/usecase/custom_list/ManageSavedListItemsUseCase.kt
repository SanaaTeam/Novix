package usecase.custom_list

import entity.Movie
import repository.SavedListRepository
import javax.inject.Inject

class ManageSavedListItemsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getItemsInSavedList(listId: Int, page: Int): List<Movie> =
        savedListRepository.getMoviesInList(listId, page)

    suspend fun addMovieToSavedList(listId: Int, movieId: Int) {
        savedListRepository.addMovieToList(listId = listId, movieId = movieId)
    }

    suspend fun removeMovieFromSavedList(listId: Int, movieId: Int) {
        savedListRepository.removeMovieFromList(listId, movieId)
    }
}