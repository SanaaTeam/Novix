package usecase

import repository.AuthenticationRepository

class CreateGuestSessionUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun createGuestSession() {
        authenticationRepository.createGuestSession()
    }
}