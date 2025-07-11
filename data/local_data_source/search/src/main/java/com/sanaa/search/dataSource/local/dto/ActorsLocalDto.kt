package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor")
data class ActorsLocalDto(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "profile_path")
    val profilePath: String?,
    @ColumnInfo(name = "biography")
    val biography: String?
) 