package manageActorUseCase

import entity.Actor
import repository.ActorRepository
import javax.inject.Inject

class GetActorDetailsUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend  operator fun invoke(id: Int): Actor =
        actorRepo.getActorDetails(id)
}