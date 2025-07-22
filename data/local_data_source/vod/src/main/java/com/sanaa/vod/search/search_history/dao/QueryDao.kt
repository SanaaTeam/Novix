package com.sanaa.vod.search.search_history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sanaa.vod.dataSource.local.search.dto.QueryLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: QueryLocalDto)

    @Query("UPDATE queries SET timestamp = :timestamp WHERE search_query = :query")
    suspend fun updateQueryTimestamp(query: String, timestamp: Long): Int

    @Transaction
    suspend fun upsertQuery(query: String, timestamp: Long) {
        val updated = updateQueryTimestamp(query, timestamp)
        if (updated == 0) {
            insertQuery(QueryLocalDto(query = query, timestamp = timestamp))
        }
    }

    @Query("SELECT * FROM queries ORDER BY timestamp DESC LIMIT :limit")
    fun getQueries(limit: Int): Flow<List<QueryLocalDto>>

    @Query("DELETE FROM queries WHERE id = :id")
    suspend fun deleteQueryById(id: Int)

    @Query("DELETE FROM queries")
    suspend fun deleteAllQueries()
}