package details.usecase.actor

import details.repository.ActorRepository
import entity.Movie

class GetActorTopMoviesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): List<Movie> = actorRepo.getActorTopMovies(id)
}