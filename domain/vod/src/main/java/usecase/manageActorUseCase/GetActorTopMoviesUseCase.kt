package usecase.manageActorUseCase

import entity.Movie
import repository.ActorRepository
import javax.inject.Inject

class GetActorTopMoviesUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(id: Int): List<Movie> =
        actorRepository.getActorTopMovies(id)
}