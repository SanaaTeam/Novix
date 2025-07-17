package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.ActorLocalDto

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: ActorLocalDto)

    @Query("SELECT * FROM actor WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    suspend fun getActorsByQuery(query: String): List<ActorLocalDto>
}