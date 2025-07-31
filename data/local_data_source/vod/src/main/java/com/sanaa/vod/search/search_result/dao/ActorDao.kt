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

    @Query("SELECT COUNT(*) FROM actor WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%'")
    suspend fun getActorsCountByQuery(query: String): Int

    @Query("SELECT * FROM actor WHERE id IN (:actorIds) LIMIT :limit OFFSET :offset")
    suspend fun getPagedActorsByIds(
        actorIds: List<Int>,
        offset: Int,
        limit: Int
    ): List<ActorLocalDto>

}