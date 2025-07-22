package home.usecase

import details.repository.TvSeriesRepository
import entity.TvSeries

class GetTopRatedTvSeriesUseCase(
    private val tvSeriesRepository: TvSeriesRepository
) {
    suspend operator fun invoke(): List<TvSeries> {
        return tvSeriesRepository.getTopRatedTvSeries()
    }
}
