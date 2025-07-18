package details.usecase.actor

import details.repository.ActorRepository
import entity.TvSeries

class GetActorTopTvSeriesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(actorId: Int): List<TvSeries> = actorRepo.getActorTopTvSeries(actorId)
}
