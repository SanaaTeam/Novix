package details.usecase.actor

import details.repository.ActorRepository
import entity.TvSeries

class GetTopTvSeriesUseCase(
    private val actorRepo: ActorRepository
) {
    suspend fun execute(id: Int): List<TvSeries> = actorRepo.getTopTvSeries(id)
}
