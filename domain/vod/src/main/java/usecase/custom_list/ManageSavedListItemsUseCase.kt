package usecase.custom_list

import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedItem
import javax.inject.Inject

class ManageSavedListItemsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getAllItemsInSavedList(listId: Int): List<SavedItem> =
        savedListRepository.getAllItemsInList(listId)

    suspend fun addMovieToSavedList(listId: Int, movieId: Int): Boolean =
        savedListRepository.addMovieToList(listId, movieId)

    suspend fun removeMovieFromSavedList(listId: Int, movieId: Int): Boolean =
        savedListRepository.removeMovieFromList(listId, movieId)
}