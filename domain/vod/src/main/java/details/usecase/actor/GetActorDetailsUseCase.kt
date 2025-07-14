package details.usecase.actor

import details.repository.ActorRepository
import entity.Actor

class GetActorDetailsUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): Actor = actorRepo.getActorDetails(id)
}