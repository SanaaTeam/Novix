package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesDetailsUseCase(
    private val repository: TvSeriesRepository
) {
    suspend fun execute(id: Int) = repository.getTvSeriesDetails(id)
}
