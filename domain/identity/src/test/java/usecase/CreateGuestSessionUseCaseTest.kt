package usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.AuthenticationRepository

class CreateGuestSessionUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var createGuestSessionUseCase: CreateGuestSessionUseCase

    @BeforeEach
    fun setUp() {
        createGuestSessionUseCase = CreateGuestSessionUseCase(authenticationRepository)
    }

    @Test
    fun `createGuestSession() should call repository`() = runTest {
        // When
        createGuestSessionUseCase.createGuestSession()

        // Then
        coVerify(exactly = 1) { authenticationRepository.createGuestSession() }
        confirmVerified(authenticationRepository)
    }

    @Test
    fun `createGuestSession() should propagate exception when repository throws`() = runTest {
        // Given
        val exception = RuntimeException("Network error")
        coEvery { authenticationRepository.createGuestSession() } throws exception

        // When & Then
        val thrown = assertThrows<RuntimeException> {
            createGuestSessionUseCase.createGuestSession()
        }
        assert(thrown.message == "Network error")
    }
}