package usecase.custom_list

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.SavedListRepository
import usecase.custom_list.custom_list_param.SavedList

class ManageSavedListsUseCaseTest {

    private val savedListRepository: SavedListRepository = mockk(relaxed = true)
    private lateinit var manageSavedListsUseCase: ManageSavedListsUseCase

    @BeforeEach
    fun setUp() {
        manageSavedListsUseCase = ManageSavedListsUseCase(savedListRepository)
    }

    @Test
    fun `getSavedLists should call repository and return list`() = runTest {
        val expected = listOf(dummyList, dummyList.copy(id = 2, title = "Favorites"))
        coEvery { savedListRepository.getSavedLists() } returns expected

        val result = manageSavedListsUseCase.getSavedLists()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSavedLists should throw when repository fails`() = runTest {
        coEvery { savedListRepository.getSavedLists() } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageSavedListsUseCase.getSavedLists()
        }
    }

    @Test
    fun `createSavedList should call repository and return SavedList`() = runTest {
        coEvery { savedListRepository.createSavedList(dummyList) } returns dummyList

        val result = manageSavedListsUseCase.createSavedList(dummyList)

        assertThat(result).isEqualTo(dummyList)
    }

    @Test
    fun `createSavedList should throw when repository fails`() = runTest {
        coEvery { savedListRepository.createSavedList(dummyList) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageSavedListsUseCase.createSavedList(dummyList)
        }
    }

    @Test
    fun `editSavedList should call repository`() = runTest {
        coEvery { savedListRepository.editSavedList(dummyList) } returns Unit

        manageSavedListsUseCase.editSavedList(dummyList)
    }

    @Test
    fun `editSavedList should throw when repository fails`() = runTest {
        coEvery { savedListRepository.editSavedList(dummyList) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageSavedListsUseCase.editSavedList(dummyList)
        }
    }

    @Test
    fun `deleteSavedList should call repository`() = runTest {
        coEvery { savedListRepository.deleteSavedList(dummyList.id) } returns Unit

        manageSavedListsUseCase.deleteSavedList(dummyList.id)
    }

    @Test
    fun `deleteSavedList should throw when repository fails`() = runTest {
        coEvery { savedListRepository.deleteSavedList(dummyList.id) } throws RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageSavedListsUseCase.deleteSavedList(dummyList.id)
        }
    }

    companion object {
        private val dummyList = SavedList(id = 1, title = "Watch-Later")
    }
}