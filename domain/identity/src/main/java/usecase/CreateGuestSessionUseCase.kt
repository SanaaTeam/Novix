package usecase

import repository.AuthenticationRepository
import javax.inject.Inject

class CreateGuestSessionUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun createGuestSession() {
        authenticationRepository.createGuestSession()
    }
}