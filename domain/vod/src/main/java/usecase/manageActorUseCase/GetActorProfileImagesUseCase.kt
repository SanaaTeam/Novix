package usecase.manageActorUseCase

import repository.ActorRepository
import javax.inject.Inject

class GetActorProfileImagesUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend operator fun invoke(id: Int, count: Int): List<String> =
        actorRepo.getProfileImageUrls(id, count)
}