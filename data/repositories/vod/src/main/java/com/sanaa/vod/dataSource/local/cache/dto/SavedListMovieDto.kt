package com.sanaa.vod.dataSource.local.cache.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_list_movies",
    indices = [
        Index(value = ["list_id", "movie_id"], unique = true)
    ]
)
data class SavedListMovieDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "movie_id")
    val movieId: Long,

    @ColumnInfo(name = "list_id")
    val listId: Long,
)
