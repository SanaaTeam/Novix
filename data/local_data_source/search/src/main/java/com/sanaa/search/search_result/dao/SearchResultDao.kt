package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.SearchResultLocalDto

@Dao
interface SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<SearchResultLocalDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: SearchResultLocalDto)

    @Query(
        """
    SELECT sr.* FROM search_result sr
    INNER JOIN searches s ON sr.id = s.id
    WHERE LOWER(s.`query`) = LOWER(:query)
      AND s.language = :language
      AND sr.item_type = :type
    """
    )
    suspend fun getByQueryAndLanguage(
        query: String,
        language: String,
        type: String
    ): List<SearchResultLocalDto>

    @Query(
        """
        DELETE FROM search_result 
        WHERE id IN (
            SELECT id FROM searches 
            WHERE timestamp < :timestamp
        )
    """
    )
    suspend fun deleteOldResults(timestamp: Long)

    @Query("DELETE FROM searches WHERE timestamp < :timestamp")
    suspend fun deleteExpiredSearches(timestamp: Long)

    @Query("DELETE FROM search_result WHERE id = :searchId")
    suspend fun deleteBySearchId(searchId: Long)
} 