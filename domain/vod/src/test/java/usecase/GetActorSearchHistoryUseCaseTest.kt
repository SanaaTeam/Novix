package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SearchHistoryRepository

class GetActorSearchHistoryUseCaseTest {
    private var searchHistoryRepository: SearchHistoryRepository = mockk(relaxed = true)
    private lateinit var getSearchHistoryUseCase: GetActorSearchHistoryUseCase

    @BeforeEach
    fun setUp() {
        getSearchHistoryUseCase = GetActorSearchHistoryUseCase(searchHistoryRepository)
    }

    @Test
    fun `execute() should call getActorSearchHistory() from SearchHistoryRepository when get searched actor history`() =
        runTest {
            // When
            getSearchHistoryUseCase.execute()

            // Then
            coVerify {
                searchHistoryRepository.getActorSearchHistory()
            }
        }
}
