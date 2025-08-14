package manageActorUseCase

import entity.TvShow
import repository.ActorRepository
import javax.inject.Inject

class GetActorTopTvShowsUseCase @Inject constructor(
    private val actorRepo: ActorRepository
) {
    suspend operator fun invoke(id: Int): List<TvShow> =
        actorRepo.getActorTopTvShows(id)
}