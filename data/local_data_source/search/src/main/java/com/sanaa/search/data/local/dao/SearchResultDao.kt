package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.data.local.entity.SearchResultEntity

@Dao
interface SearchResultDao {

    @Query("SELECT * FROM search_results WHERE `query` = :query AND `language` = :language")
    suspend fun getByQueryAndLanguage(query: String, language: String): List<SearchResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(results: List<SearchResultEntity>)

    @Query("DELETE FROM search_results WHERE timestamp < :threshold")
    suspend fun deleteOldResults(threshold: Long)
} 