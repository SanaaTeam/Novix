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

    @Query(
        """
        SELECT * FROM tv_series 
        WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
        ORDER BY 
            CASE 
                WHEN LOWER(title) = LOWER(:query) THEN 1
                WHEN LOWER(title) LIKE LOWER(:query) || '%' THEN 2
                ELSE 3
            END,
            title ASC
        LIMIT :limit OFFSET :offset
        """
    )
    suspend fun getFilteredSeries(query: String, limit: Int, offset: Int): List<TvSeriesLocalDto>

} 