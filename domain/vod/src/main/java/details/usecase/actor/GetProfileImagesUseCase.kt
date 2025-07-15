package details.usecase.actor

import details.repository.ActorRepository

class GetProfileImagesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): List<String> = actorRepo.getProfileImages(id)
}