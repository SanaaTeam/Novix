package home.usecase

import entity.TvSeries
import home.repository.HomeRepository
import kotlinx.coroutines.flow.Flow

class GetTrendingTvSeriesUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<List<TvSeries>> {
        return homeRepository.getTrendingTvSeries()
    }
}