package usecase

import entity.Actor
import entity.Genre
import entity.Review
import entity.Season
import entity.TvShow
import repository.TvSeriesRepository
import javax.inject.Inject

class ManageTvSeriesUseCase @Inject constructor(
    private val tvSeriesRepo: TvSeriesRepository
) {
    suspend fun getTvSeriesByGenre(page: Int, genreId: Int): List<TvShow> =
        tvSeriesRepo.getTvSeriesByGenre(page, genreId)

    suspend fun getTvSeriesDetails(seriesId: Int): TvShow =
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

    suspend fun getPopularSeries(page: Int): List<TvShow> {
        return tvSeriesRepo.getPopularSeries(page)
    }

    suspend fun getTopRatedTvSeries(page: Int, genreId: Int?): List<TvShow> {
        return tvSeriesRepo.getTopRatedTvSeries(page, genreId)
    }

    suspend fun getTrendingTvSeries(page: Int, genreId: Int?): List<TvShow> {
        return tvSeriesRepo.getTrendingTvSeries(page, genreId)
    }

    suspend fun getSeriesGenres(): List<Genre> {
        return tvSeriesRepo.getSeriesGenres()
    }

    suspend fun getSeriesRate(accountId: Long, seriesId: Int): Int {
        return tvSeriesRepo.getSeriesRate(accountId, seriesId) ?: 0
    }

    suspend fun getEpisodesRate(accountId: Long, seasonNumber: Int, episodeNumber: Int): Int {
        return tvSeriesRepo.getEpisodesRate(accountId, seasonNumber, episodeNumber) ?: 0
    }

    suspend fun addTvSeriesRate(seriesId: Int, rating: Float): Boolean {
        return tvSeriesRepo.addTvSeriesRate(seriesId = seriesId, rating = rating)
    }

    suspend fun getUserRatedTvSeries(accountId: Long, sessionId: String): List<TvShow> {
        return tvSeriesRepo.getUserRatedTvSeries(accountId,sessionId)
    }

    suspend fun deleteTvSeriesRate(seriesId: Int): Boolean {
        return tvSeriesRepo.deleteTvSeriesRate(seriesId)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
