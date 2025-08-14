package manageActorUseCase

import entity.Actor
import repository.ActorRepository
import javax.inject.Inject

class GetTrendingActorsUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend operator fun invoke(page: Int): List<Actor> {
        return actorRepo.getTrendingActors(page)
    }
}