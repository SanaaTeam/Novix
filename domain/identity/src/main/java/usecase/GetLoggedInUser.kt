package usecase

import entity.User
import repository.AuthenticationRepository

class GetLoggedInUserUseCase(
    private val repository: AuthenticationRepository
) {
    suspend fun getLoggedInUser(): User {
        return repository.getLoggedUser()
    }
}