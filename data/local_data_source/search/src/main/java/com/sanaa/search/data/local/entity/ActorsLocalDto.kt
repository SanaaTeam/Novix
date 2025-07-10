package com.sanaa.search.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actors")
data class ActorsLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "actor_id")
    val actorId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "image_path")
    val imagePath: String?
) 