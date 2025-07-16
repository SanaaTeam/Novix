package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesCastUseCase(
    private val repository: TvSeriesRepository

) {
    suspend fun execute(id: Int) = repository.getTvSeriesCast(id)
}