package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesImagesUseCase(
    private val repository: TvSeriesRepository
) {
    suspend fun execute(id: Int, count: Int) = repository.getTvSeriesImages(id, count)
}