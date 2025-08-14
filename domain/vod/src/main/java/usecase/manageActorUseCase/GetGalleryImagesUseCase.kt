package usecase.manageActorUseCase

import repository.ActorRepository
import javax.inject.Inject

class GetGalleryImagesUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend operator fun invoke(id: Int): List<String> =
        actorRepo.getGalleryImageUrls(id)
}