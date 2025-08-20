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

class LoginUseCaseTest {

    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(authenticationRepository)
    }

    @Test
    fun `login() should call repository with correct credentials`() = runTest {
        val userName = "Novix User"
        val password = "password"

        loginUseCase.login(userName, password)

        coVerify(exactly = 1) { authenticationRepository.login(userName, password) }
        confirmVerified(authenticationRepository)
    }

    @Test
    fun `login() should propagate exception when repository throws`() = runTest {
        val userName = "user"
        val password = "wrong-pass"
        val exception = RuntimeException("Network error")
        coEvery { authenticationRepository.login(userName, password) } throws exception

        val thrown = assertThrows<RuntimeException> {
            loginUseCase.login(userName, password)
        }
        assert(thrown.message == "Network error")
    }
}