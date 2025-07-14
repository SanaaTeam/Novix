package details.usecase.tv_series

import details.repository.TvSeriesRepository

class GetTvSeriesReviewsUseCase(
    private val repository: TvSeriesRepository

) {
    suspend fun execute(id: Int) = repository.getTvSeriesReviews(id)
}