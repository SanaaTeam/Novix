package usecase

import kotlinx.coroutines.flow.Flow
import repository.AuthenticationRepository
import javax.inject.Inject

class CheckIfUserIsLoggedInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository
) {
    fun isLoggedIn(): Flow<Boolean> {
        return authenticationRepository.isLoggedIn()
    }
}