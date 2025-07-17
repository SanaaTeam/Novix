package com.sanaa.search.search_history.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.QueryLocalDto
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: QueryLocalDto)

    @Query("SELECT * FROM queries ORDER BY timestamp DESC LIMIT :limit")
    fun getQueries(limit: Int): Flow<List<QueryLocalDto>>

    @Query("DELETE FROM queries WHERE id = :id")
    suspend fun deleteQueryById(id: Int)

    @Query("DELETE FROM queries")
    suspend fun deleteAllQueries()
}