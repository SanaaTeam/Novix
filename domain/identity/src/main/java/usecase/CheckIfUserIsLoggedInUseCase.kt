package usecase

import repository.AuthenticationRepository

class CheckIfUserIsLoggedInUseCase(
    private val repository: AuthenticationRepository
) {
    suspend fun isLoggedIn(): Boolean {
        return repository.isLoggedIn()
    }
}