package usecase

import entity.Actor
import entity.Genre
import entity.Review
import entity.Season
import entity.TvShow
import repository.TvShowRepository
import javax.inject.Inject

class ManageTvShowUseCase @Inject constructor(
    private val repository: TvShowRepository
) {
    suspend fun getTvShowsByGenre(page: Int, genreId: Int): List<TvShow> =
        repository.getTvShowsByGenre(page, genreId)

    suspend fun getTvShowDetails(id: Int): TvShow =
        repository.getTvShowDetails(id)

    suspend fun getTvShowCast(id: Int): List<Actor> =
        repository.getTvShowCast(id)

    suspend fun getTvShowImageUrls(id: Int): List<String> =
        repository.getTvShowImageUrls(id, IMAGE_COUNT)

    suspend fun getTvShowReviews(tvShowId: Int, page: Int): List<Review> =
        repository.getTvShowReviews(tvShowId, page)

    suspend fun getTvShowSeasonDetails(tvShowId: Int, seasonNumber: Int): Season =
        repository.getTvShowSeason(tvShowId, seasonNumber)

    suspend fun getTvShowTrailer(tvShowId: Int): String? =
        repository.getTvShowTrailer(tvShowId)

    suspend fun getPopularTvShows(page: Int): List<TvShow> {
        return repository.getPopularTvShows(page)
    }

    suspend fun getTopRatedTvShows(page: Int, genreId: Int?): List<TvShow> {
        return repository.getTopRatedTvShows(page, genreId)
    }

    suspend fun getTrendingTvShows(page: Int, genreId: Int?): List<TvShow> {
        return repository.getTrendingTvShows(page, genreId)
    }

    suspend fun getTvShowGenres(): List<Genre> {
        return repository.getTvShowGenres()
    }

    suspend fun getTvShowRating(accountId: Long, tvShowId: Int): Int {
        return repository.getTvShowRate(accountId, tvShowId) ?: 0
    }

    suspend fun getEpisodesRate(accountId: Long, seasonNumber: Int, episodeNumber: Int): Int {
        return repository.getEpisodesRate(accountId, seasonNumber, episodeNumber) ?: 0
    }

    suspend fun addTvShowRate(tvShowId: Int, rating: Float): Boolean {
        return repository.addTvShowRate(tvShowId = tvShowId, rating = rating)
    }

    suspend fun getRatedTvShows(accountId: Long, sessionId: String): List<TvShow> {
        return repository.getRatedTvShows(accountId, sessionId)
    }

    suspend fun deleteTvShowRate(tvShowId: Int): Boolean {
        return repository.deleteTvShowRate(tvShowId)
    }

    private companion object {
        const val IMAGE_COUNT = 10
    }
}
