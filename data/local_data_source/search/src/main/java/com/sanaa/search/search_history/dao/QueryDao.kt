package com.sanaa.search.search_history.dao

import androidx.room.Dao
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {
    @Query("""
        INSERT INTO queries (search_query, timestamp) 
        VALUES (:query, :timestamp)
        ON CONFLICT(search_query) 
        DO UPDATE SET timestamp = :timestamp
    """)
    suspend fun upsertQueryWithTimestamp(query: String, timestamp: Long)

    @Query("SELECT * FROM queries ORDER BY timestamp DESC LIMIT :limit")
    fun getAllQueries(limit: Int): Flow<List<QueryLocalDto>>

    @Query("DELETE FROM queries WHERE id = :id")
    suspend fun deleteQueryById(id: Int)

    @Query("DELETE FROM queries")
    suspend fun deleteAllQueries()
}