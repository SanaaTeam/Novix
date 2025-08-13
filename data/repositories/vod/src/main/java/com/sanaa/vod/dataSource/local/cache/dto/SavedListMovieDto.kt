package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_list_movies",
    indices = [
        Index(value = ["movie_id"], unique = true),
        Index(value = ["list_id"])
    ]
)
data class SavedListMovieDto(
    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    val movieId: Long,

    @ColumnInfo(name = "list_id")
    val listId: Long,
)
