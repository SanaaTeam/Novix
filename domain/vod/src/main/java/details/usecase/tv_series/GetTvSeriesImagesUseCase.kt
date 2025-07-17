package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesImagesUseCase(
    private val repository: TvSeriesRepository
) {
    suspend fun execute(id: Int) = repository.getTvSeriesImages(id, COUNT)

    private companion object {
        const val COUNT = 5
    }
}