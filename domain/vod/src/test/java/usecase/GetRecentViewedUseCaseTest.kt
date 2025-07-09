package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class GetRecentViewedUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getRecentViewedUseCase: GetRecentViewedUseCase

    @BeforeEach
    fun setUp() {
        getRecentViewedUseCase = GetRecentViewedUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call getRecentViewed() from SearchHistoryRepository when get recent viewed movies`() =
        runTest {
            // When
            getRecentViewedUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.getRecentViewed()
            }
        }
}