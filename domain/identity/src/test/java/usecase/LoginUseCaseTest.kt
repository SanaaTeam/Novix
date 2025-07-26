package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.AuthenticationRepository

class LoginUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    @Test
    fun `login() should use AuthenticationRepository when try to login`() = runTest {
        // Given
        val userName = "Novix User"
        val password = "password"

        // When
        loginUseCase.login(userName, password)

        // Then
        coVerify { authenticationRepository.login(userName, password) }
    }
}