package usecase

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import repository.AuthenticationRepository
import kotlin.test.Test

class LogOutUseCaseTest {
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private  val logoutUseCase: LogOutUseCase = LogOutUseCase(authenticationRepository)

    @Test
    fun `logout should call repository logout`() = runTest{
        logoutUseCase.logout()
        coVerify(exactly = 1) { authenticationRepository.logout() }
    }

}