package repository

import model.Language
import model.MediaFilters
import model.TvSeries

interface TvSeriesRepository {
    suspend fun searchTvSeries(query: String, filters: MediaFilters?, language: Language): List<TvSeries>
}
