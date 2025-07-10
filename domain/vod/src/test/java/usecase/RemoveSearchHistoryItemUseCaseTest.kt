package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class RemoveSearchHistoryItemUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var removeSearchHistoryItemUseCase: RemoveSearchHistoryItemUseCase

    @BeforeEach
    fun setUp() {
        removeSearchHistoryItemUseCase = RemoveSearchHistoryItemUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call removeSearchHistoryItem() from SearchHistoryRepository when remove item form the search history`() =
        runTest {
            // Given
            val id = 1L

            // When
            removeSearchHistoryItemUseCase.execute(id)

            // Then
            coVerify {
                searchHistoryRepository.removeSearchHistoryItem(id)
            }
        }
}