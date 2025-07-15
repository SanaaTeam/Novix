package details.usecase.actor

import details.repository.ActorRepository
import entity.TvSeries

class GetActorTopTvSeriesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): List<TvSeries> = actorRepo.getActorTopTvSeries(id)
}
