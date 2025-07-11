package com.sanaa.search.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.search.data.local.entity.ActorsLocalDto

@Dao
interface ActorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: ActorsLocalDto)

    @Query("SELECT * FROM actor WHERE name LIKE '%' || :query || '%' LIMIT 1")
    suspend fun getActorByQuery(query: String): ActorsLocalDto?
}