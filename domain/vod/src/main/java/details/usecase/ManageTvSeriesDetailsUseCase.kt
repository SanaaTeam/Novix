package details.usecase

import details.repository.TvSeriesRepository
import entity.Actor
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries

class ManageTvSeriesDetailsUseCase(
    private val tvSeriesRepo: TvSeriesRepository
) {
    suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> =
        tvSeriesRepo.getTvSeriesByGenre(genre)

    suspend fun getTvSeriesDetails(seriesId: Int): TvSeries =
        tvSeriesRepo.getTvSeriesDetails(seriesId)

    suspend fun getTvSeriesCast(seriesId: Int): List<Actor> =
        tvSeriesRepo.getTvSeriesCast(seriesId)

    suspend fun getTvSeriesImages(seriesId: Int): List<String> =
        tvSeriesRepo.getTvSeriesImages(seriesId, IMAGE_COUNT)

    suspend fun getTvSeriesReviews(seriesId: Int): List<Review> =
        tvSeriesRepo.getTvSeriesReviews(seriesId)

    suspend fun getTvSeriesSeasonDetails(seriesId: Int, seasonNumber: Int): Season =
        tvSeriesRepo.getTvSeriesSeason(seriesId, seasonNumber)

    suspend fun getTvSeriesTrailer(seriesId: Int): String? =
        tvSeriesRepo.getTvSeriesTrailer(seriesId)

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
