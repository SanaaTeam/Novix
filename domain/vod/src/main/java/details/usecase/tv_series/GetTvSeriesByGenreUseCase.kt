package details.usecase.tv_series

import details.repository.TvSeriesRepository
import entity.Genre
import entity.TvSeries

class GetTvSeriesByGenreUseCase(
    private val repository: TvSeriesRepository
) {
    suspend fun execute(genre: Genre): List<TvSeries> = repository.getTvSeriesByGenre(genre)
}