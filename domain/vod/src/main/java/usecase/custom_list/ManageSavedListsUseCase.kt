package usecase.custom_list

import kotlinx.coroutines.flow.Flow
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class ManageSavedListsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    fun observeSavedLists(): Flow<List<SavedList>> =
        savedListRepository.observeSavedLists()

    suspend fun getSavedListsOnce(): List<SavedList> =
        savedListRepository.getSavedListsOnce()

    suspend fun createSavedList(title: String): SavedList =
        savedListRepository.createSavedList(title)

    suspend fun deleteSavedList(listId: Int) =
        savedListRepository.deleteSavedList(listId)
}