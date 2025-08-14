package usecase.custom_list

import entity.Movie
import kotlinx.coroutines.flow.Flow
import repository.SavedListRepository
import javax.inject.Inject

class ManageSavedListItemsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    fun observeMoviesInList(listId: Int): Flow<List<Movie>> =
        savedListRepository.observeMoviesInList(listId)

    suspend fun isMovieSaved(movieId: Int): Boolean =
        savedListRepository.isMovieSaved(movieId)

    suspend fun getMoviesInList(listId: Int, page: Int): List<Movie> =
        savedListRepository.getMoviesInList(listId, page)

    suspend fun addMovieToSavedList(listId: Int, movieId: Int) =
        savedListRepository.addMovieToList(listId, movieId)

    suspend fun removeMovieFromSavedList(listId: Int, movieId: Int) =
        savedListRepository.removeMovieFromList(listId, movieId)
}