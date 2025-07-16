package details.usecase.tv_series


import details.repository.TvSeriesRepository
import entity.Season

class GetTvSeriesSeasonDetailsUseCase(
    private val repo: TvSeriesRepository
) {
    suspend fun execute(id: Int, seasonNumber: Int): Season {
        return repo.getTvSeriesSeason(id, seasonNumber)
    }
}