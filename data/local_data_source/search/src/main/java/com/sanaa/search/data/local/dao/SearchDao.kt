package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.SearchLocalDto

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchLocalDto): Int

    @Query("SELECT * FROM searches WHERE `query` = :query AND language = :language LIMIT 1")
    suspend fun getSearchByQueryAndLanguage(query: String, language: String): SearchLocalDto?

    @Query("SELECT * FROM searches ORDER BY timestamp DESC")
    suspend fun getAllSearches(): List<SearchLocalDto>

    @Query("SELECT * FROM searches WHERE `query` LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    suspend fun getSearchesByQuery(query: String): List<SearchLocalDto>
} 