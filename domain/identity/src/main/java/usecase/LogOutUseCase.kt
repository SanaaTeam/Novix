package usecase

import javax.inject.Inject
import repository.AuthenticationRepository

class LogOutUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun logout() = authenticationRepository.logout()
        authenticationRepository.logout()
    }
}