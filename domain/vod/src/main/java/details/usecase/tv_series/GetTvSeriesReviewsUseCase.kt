package details.usecase.tv_series

import details.repository.TvSeriesRepository
import entity.Review

class GetTvSeriesReviewsUseCase(
    private val repository: TvSeriesRepository

) {
    suspend fun execute(id: Int): List<Review> = repository.getTvSeriesReviews(id)
}