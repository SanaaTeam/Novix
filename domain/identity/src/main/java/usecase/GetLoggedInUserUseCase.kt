package usecase

import entity.User
import repository.AuthenticationRepository
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend fun getLoggedInUser(): User {
        return authenticationRepository.getLoggedUser()
    }
}