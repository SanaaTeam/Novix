package usecase.manageActorUseCase

import entity.Actor
import repository.ActorRepository
import javax.inject.Inject

class GetTrendingActorsUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(page: Int): List<Actor> {
        return actorRepository.getTrendingActors(page)
    }
}