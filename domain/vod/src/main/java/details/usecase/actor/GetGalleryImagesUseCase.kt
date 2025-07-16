package details.usecase.actor

import details.repository.ActorRepository

class GetGalleryImagesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): List<String> = actorRepo.getGalleryImages(id)
}
