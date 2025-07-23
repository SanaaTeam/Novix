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
    suspend fun getTvSeriesByGenre(genre: Genre): List<TvSeries> =
        tvSeriesRepo.getTvSeriesByGenre(genre)

    suspend fun getTvSeriesDetails(seriesId: Int): TvSeries =
        tvSeriesRepo.getTvSeriesDetails(seriesId)

    suspend fun getTvSeriesCast(seriesId: Int): List<Actor> =
        tvSeriesRepo.getTvSeriesCast(seriesId)

    suspend fun getTvSeriesImages(seriesId: Int): List<String> =
        tvSeriesRepo.getTvSeriesImageUrls(seriesId, IMAGE_COUNT)

    suspend fun getTvSeriesReviews(seriesId: Int): List<Review> =
        tvSeriesRepo.getTvSeriesReviews(seriesId)

    suspend fun getTvSeriesSeasonDetails(seriesId: Int, seasonNumber: Int): Season =
        tvSeriesRepo.getTvSeriesSeason(seriesId, seasonNumber)

    suspend fun getTvSeriesTrailer(seriesId: Int): String? =
        tvSeriesRepo.getTvSeriesTrailer(seriesId)

    suspend fun getPopularSeries(page: Int, genre: Genre?): List<TvSeries> {
        return tvSeriesRepo.getPopularSeries(page, genre)
    }

    suspend fun getTopRatedTvSeries(page: Int, genre: Genre?): List<TvSeries> {
        return tvSeriesRepo.getTopRatedTvSeries(page, genre)
    }

    suspend fun getTrendingTvSeries(page: Int, genre: Genre?): List<TvSeries> {
        return tvSeriesRepo.getTrendingTvSeries(page, genre)
    }

    suspend fun getSeriesGenres(): List<Genre> {
        return tvSeriesRepo.getSeriesGenres()
    }


    private companion object {
        const val IMAGE_COUNT = 10
    }
}
