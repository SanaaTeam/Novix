package usecase

import entity.Actor
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries
import repository.TvSeriesRepository

class ManageTvSeriesUseCase(
    private val tvSeriesRepo: TvSeriesRepository
) {
    suspend fun getTvSeriesByGenre(page: Int, genreId: Int): List<TvSeries> =
        tvSeriesRepo.getTvSeriesByGenre(page, genreId)

    suspend fun getTvSeriesDetails(seriesId: Int): TvSeries =
        tvSeriesRepo.getTvSeriesDetails(seriesId)

    suspend fun getTvSeriesCast(seriesId: Int): List<Actor> =
        tvSeriesRepo.getTvSeriesCast(seriesId)

    suspend fun getTvSeriesImages(seriesId: Int): List<String> =
        tvSeriesRepo.getTvSeriesImageUrls(seriesId, IMAGE_COUNT)

    suspend fun getTvSeriesReviews(seriesId: Int, page: Int): List<Review> =
        tvSeriesRepo.getTvSeriesReviews(seriesId, page)

    suspend fun getTvSeriesSeasonDetails(seriesId: Int, seasonNumber: Int): Season =
        tvSeriesRepo.getTvSeriesSeason(seriesId, seasonNumber)

    suspend fun getTvSeriesTrailer(seriesId: Int): String? =
        tvSeriesRepo.getTvSeriesTrailer(seriesId)

    suspend fun getPopularSeries(page: Int): List<TvSeries> {
        return tvSeriesRepo.getPopularSeries(page)
    }

    suspend fun getTopRatedTvSeries(page: Int, genreId: Int?): List<TvSeries> {
        return tvSeriesRepo.getTopRatedTvSeries(page, genreId)
    }

    suspend fun getTrendingTvSeries(page: Int, genreId: Int?): List<TvSeries> {
        return tvSeriesRepo.getTrendingTvSeries(page, genreId)
    }

    suspend fun getSeriesGenres(): List<Genre> {
        return tvSeriesRepo.getSeriesGenres()
    }

    suspend fun addTvSeriesRate(seriesId: Int, rating: Float): Int {
        return tvSeriesRepo.addTvSeriesRate(seriesId = seriesId, rating = rating)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
