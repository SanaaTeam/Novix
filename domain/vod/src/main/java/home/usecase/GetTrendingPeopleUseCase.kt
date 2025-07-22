package home.usecase

import details.repository.ActorRepository
import entity.Actor

class GetTrendingPeopleUseCase(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(): List<Actor> {
        return actorRepository.getTrendingActors()
    }
}