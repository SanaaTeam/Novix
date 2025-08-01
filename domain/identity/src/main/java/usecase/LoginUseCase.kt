package usecase

import repository.AuthenticationRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun login(userName: String, password: String) {
        authenticationRepository.login(userName, password)
    }
}