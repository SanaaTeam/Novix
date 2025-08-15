package usecase.manageActorUseCase

import repository.ActorRepository
import javax.inject.Inject

class GetActorProfileImagesUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(id: Int, count: Int): List<String> =
        actorRepository.getProfileImageUrls(id, count)
}