package usecase.custom_list

import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class ManageSavedListsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getSavedLists(accountId: Long): List<SavedList> =
        savedListRepository.getSavedLists(accountId)

    suspend fun createSavedList(list: SavedList): SavedList =
        savedListRepository.createSavedList(list)

    suspend fun deleteSavedList(listId: Int) =
        savedListRepository.deleteSavedList(listId)
}