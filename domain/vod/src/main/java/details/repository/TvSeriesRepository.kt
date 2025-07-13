package details.repository

import entity.Episode
import entity.Genre
import entity.Review
import entity.Season
import entity.TvSeries

interface TvSeriesRepository {
    suspend fun getTvSeriesDetails(id: Int): TvSeries
    suspend fun getTvSeriesReviews(id: Int): List<Review>
    suspend fun getTvSeriesImages(id: Int): List<String>
    suspend fun getTvSeriesByCategory(category: Genre): List<TvSeries>
    suspend fun getTvSeriesSeasons(id: Int): List<Season>
    suspend fun getSeasonEpisodes(seriesId: Int, seasonId: Int): List<Episode>
}