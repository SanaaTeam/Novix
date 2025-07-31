package usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.AuthenticationRepository

class CheckIfUserIsLoggedInUseCaseTest {
    private val repository: AuthenticationRepository = mockk()
    private lateinit var useCase: CheckIfUserIsLoggedInUseCase

    @BeforeEach
    fun setUp() {
        useCase = CheckIfUserIsLoggedInUseCase(repository)
    }

    @Test
    fun `isLoggedIn() should return true when user is logged in`() = runTest {
        coEvery { repository.isLoggedIn() } returns true

        val result = useCase.isLoggedIn()

        assertThat(result).isTrue()
    }

    @Test
    fun `isLoggedIn() should return false when user is not logged in`() = runTest {
        coEvery { repository.isLoggedIn() } returns false

        val result = useCase.isLoggedIn()

        assertThat(result).isFalse()
    }
}