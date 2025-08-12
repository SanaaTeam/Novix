package usecase.custom_list

import com.google.common.truth.Truth.assertThat
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
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
        val expected = listOf(DUMMY_LIST, DUMMY_LIST.copy(id = 2, title = "Faves"))
        coEvery { savedListRepository.getSavedLists() } returns expected

        val result = manageSavedListsUseCase.getSavedLists()

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `getSavedLists should throw when repository fails`() = runTest {
        coEvery { savedListRepository.getSavedLists() } throws
                RetrievingDataFailureException("Error")

        assertThrows<RetrievingDataFailureException> {
            manageSavedListsUseCase.getSavedLists()
        }
    }

    @Test
    fun `createSavedList should call repository and return SavedList`() = runTest {
        coEvery { savedListRepository.createSavedList(DUMMY_LIST.title) } returns DUMMY_LIST

        val result = manageSavedListsUseCase.createSavedList(DUMMY_LIST.title)

        assertThat(result).isEqualTo(DUMMY_LIST)
    }

    @Test
    fun `deleteSavedList should call repository`() = runTest {
        coEvery { savedListRepository.deleteSavedList(DUMMY_LIST.id) } returns Unit

        manageSavedListsUseCase.deleteSavedList(DUMMY_LIST.id)

        coVerify(exactly = 1) { savedListRepository.deleteSavedList(DUMMY_LIST.id) }
    }

    private companion object {
        val DUMMY_LIST = SavedList(id = 1, title = "Watch-Later", itemCount = 5)
    }
}