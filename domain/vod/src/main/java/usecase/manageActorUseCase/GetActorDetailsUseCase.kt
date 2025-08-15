package usecase.manageActorUseCase

import entity.Actor
import repository.ActorRepository
import javax.inject.Inject

class GetActorDetailsUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend  operator fun invoke(id: Int): Actor =
        actorRepository.getActorDetails(id)
}