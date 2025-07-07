package repository

import model.MediaFilters
import model.TvSeries

interface TvSeriesRepository {
    suspend fun searchTvSeries(query: String, filters: MediaFilters?, language: String): List<TvSeries>
}
