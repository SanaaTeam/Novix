package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository
import usecase.search.RecentViewedItem

class AddRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var addRecentViewedUseCase: AddRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        addRecentViewedUseCase = AddRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call addRecentViewedItem() from SearchHistoryRepository when add recent viewed movies`(): Unit =
        runTest {
            // Given
            val item = RecentViewedItem(1L, "https://image.com")

            // When
            addRecentViewedUseCase.execute(item)

            // Then
            coVerify {
                searchHistoryRepository.addRecentViewedItem(item)
            }
        }
}