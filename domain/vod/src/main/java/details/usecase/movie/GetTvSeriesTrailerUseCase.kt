package details.usecase.movie

import details.repository.TvSeriesRepository

class GetTvSeriesTrailerUseCase(private val repository: TvSeriesRepository) {
    suspend operator fun invoke(id: Int): String? {
        return repository.getTvSeriesTrailer(id)
    }
}