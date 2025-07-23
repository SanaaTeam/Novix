package com.sanaa.vod.search.search_result.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanaa.vod.dataSource.local.search.dto.ActorLocalDto

@Dao
interface ActorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActor(actor: ActorLocalDto)

    @Query("SELECT * FROM actor WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    suspend fun getActorsByQuery(query: String): List<ActorLocalDto>

    @Query(
        "SELECT * FROM actor " +
                "WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' " +
                "ORDER BY " +
                "CASE " +
                "WHEN LOWER(name) = LOWER(:query) THEN 1 " +
                "WHEN LOWER(name) LIKE LOWER(:query) || '%' THEN 2 " +
                "ELSE 3 " +
                "END, " +
                "name ASC " +
                "LIMIT :limit OFFSET :offset"
    )
    suspend fun getPagedActorsByQuery(
        query: String,
        limit: Int,
        offset: Int
    ): List<ActorLocalDto>

}