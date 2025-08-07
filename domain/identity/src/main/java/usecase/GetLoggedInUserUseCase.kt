package usecase

import entity.User
import kotlinx.coroutines.flow.Flow
import repository.AuthenticationRepository
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
     fun getLoggedInUser(): Flow<User> {
        return authenticationRepository.getLoggedUser()
    }
}