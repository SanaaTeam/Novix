package details.usecase.tv_series


import details.repository.TvSeriesRepository
import entity.Season

class GetTvSeriesSeasonsUseCase(
    private val repo: TvSeriesRepository
) {
    suspend fun execute(id: Int, seasonNumber: Int): List<Season> {
        return repo.getTvSeriesSeason(id, seasonNumber)
    }
}