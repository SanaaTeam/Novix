package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class ClearRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var addRecentViewedUseCase: ClearRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        addRecentViewedUseCase = ClearRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call clearRecentViewed() from SearchHistoryRepository when clear recent viewed movies`() =
        runTest {
            // When
            addRecentViewedUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.clearRecentViewed()
            }
        }
}