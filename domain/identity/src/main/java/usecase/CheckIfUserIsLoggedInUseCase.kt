package usecase

import repository.AuthenticationRepository
import javax.inject.Inject

class CheckIfUserIsLoggedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun isLoggedIn(): Boolean {
        return authenticationRepository.isLoggedIn()
    }
}