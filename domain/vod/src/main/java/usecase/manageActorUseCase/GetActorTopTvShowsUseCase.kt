package usecase.manageActorUseCase

import entity.TvShow
import repository.ActorRepository
import javax.inject.Inject

class GetActorTopTvShowsUseCase @Inject constructor(
    private val actorRepository: ActorRepository
) {
    suspend operator fun invoke(id: Int): List<TvShow> =
        actorRepository.getActorTopTvShows(id)
}