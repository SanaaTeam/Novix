package details.usecase.tv_series


import details.repository.TvSeriesRepository
import entity.Episode

class GetEpisodeDetailsUseCase(
    private val repo: TvSeriesRepository
) {
    suspend fun execute(id: Int, seasonNumber: Int, episodeNumber: Int): Episode {
        return repo.getEpisodeDetails(id, seasonNumber, episodeNumber)
    }
}