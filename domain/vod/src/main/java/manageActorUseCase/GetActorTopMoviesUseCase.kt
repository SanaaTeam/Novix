package manageActorUseCase

import entity.Movie
import repository.ActorRepository
import javax.inject.Inject

class GetActorTopMoviesUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend operator fun invoke(id: Int): List<Movie> =
        actorRepo.getActorTopMovies(id)
}