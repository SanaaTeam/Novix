package com.sanaa.vod.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.search.dto.TvSeriesLocalDto

@Dao
interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: TvSeriesLocalDto)

    @Query("SELECT * FROM tv_series WHERE id IN (:tvSeriesIds) LIMIT :limit OFFSET :offset")
    suspend fun getPagedTvSeriesByIds(
        tvSeriesIds: List<Int>,
        limit: Int,
        offset: Int
    ): List<TvSeriesLocalDto>
} 