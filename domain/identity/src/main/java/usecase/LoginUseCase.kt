package usecase

import repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun login(userName: String, password: String) {
        authenticationRepository.login(userName, password)
    }
}