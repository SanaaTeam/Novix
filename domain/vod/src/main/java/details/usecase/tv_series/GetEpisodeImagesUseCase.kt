package details.usecase.tv_series


import details.repository.TvSeriesRepository

class GetEpisodeImagesUseCase(
    private val repo: TvSeriesRepository
) {
    suspend fun execute(id: Int, seasonNumber: Int, episodeNumber: Int, count: Int): List<String> {
        return repo.getEpisodeImages(id, seasonNumber, episodeNumber, count)
    }
}