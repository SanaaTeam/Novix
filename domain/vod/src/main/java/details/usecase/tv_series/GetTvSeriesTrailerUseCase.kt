package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesTrailerUseCase(private val repository: TvSeriesRepository) {
    suspend fun execute(seriesId: Int): String? {
        return repository.getTvSeriesTrailer(seriesId)
    }
}