package usecase.custom_list

import kotlinx.coroutines.flow.Flow
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList
import javax.inject.Inject

class ManageSavedListsUseCase @Inject constructor(
    private val savedListRepository: SavedListRepository
) {
    suspend fun getSavedLists(): Flow<List<SavedList>> =
        savedListRepository.getLocalSavedLists()

    suspend fun refreshSavedList() = savedListRepository.refreshSavedLists()
    suspend fun clearData() = savedListRepository.clearAllLists()

    suspend fun createSavedList(title: String) =
        savedListRepository.createSavedList(title)

    suspend fun deleteSavedList(listId: Int) =
        savedListRepository.deleteSavedList(listId)
}