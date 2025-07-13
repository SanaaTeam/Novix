package com.sanaa.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.dataSource.local.dto.ActorsLocalDto

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: ActorsLocalDto)

    @Query("SELECT * FROM actor WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    suspend fun getActorByQuery(query: String): List<ActorsLocalDto>
}