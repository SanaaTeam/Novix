package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sanaa.search.dataSource.local.dto.SearchLocalDto

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchLocalDto): Long

    @Update
    suspend fun updateSearch(search: SearchLocalDto)

    @Query("SELECT * FROM searches WHERE LOWER(`query`) = LOWER(:query) AND language = :language LIMIT 1")
    suspend fun getSearchByQueryAndLanguage(query: String, language: String): SearchLocalDto?

    @Query("SELECT * FROM searches ORDER BY timestamp DESC")
    suspend fun getAllSearches(): List<SearchLocalDto>


    @Query("UPDATE searches SET timestamp = :timestamp WHERE `query` = :query AND language = :language")
    suspend fun updateTimestamp(query: String, language: String, timestamp: Long)

    @Query("DELETE FROM searches WHERE timestamp < :timestamp")
    suspend fun deleteExpiredSearches(timestamp: Long)
} 