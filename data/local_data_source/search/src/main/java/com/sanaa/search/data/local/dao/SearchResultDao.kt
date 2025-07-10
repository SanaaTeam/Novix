package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.data.local.entity.SearchResultLocalDto

@Dao
interface SearchResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<SearchResultLocalDto>)

    @Query("""
        SELECT sr.* FROM search_results sr
        INNER JOIN searches s ON sr.search_id = s.search_id
        WHERE s.query = :query AND s.language = :language
    """)
    suspend fun getByQueryAndLanguage(query: String, language: String): List<SearchResultLocalDto>

    @Query("""
        DELETE FROM search_results 
        WHERE search_id IN (
            SELECT search_id FROM searches 
            WHERE timestamp < :timestamp
        )
    """)
    suspend fun deleteOldResults(timestamp: Long)

    @Query("DELETE FROM search_results WHERE search_id = :searchId")
    suspend fun deleteBySearchId(searchId: Long)
} 