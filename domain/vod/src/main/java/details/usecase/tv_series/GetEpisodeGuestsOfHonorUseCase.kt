package details.usecase.tv_series



import details.repository.TvSeriesRepository
import entity.Actor

class GetEpisodeGuestsOfHonorUseCase(
    private val repo: TvSeriesRepository
) {
    suspend fun execute(id: Int, seasonNumber: Int, episodeNumber: Int): List<Actor> {
        return repo.getEpisodeGuestsOfHonor(id, seasonNumber, episodeNumber)
    }
}