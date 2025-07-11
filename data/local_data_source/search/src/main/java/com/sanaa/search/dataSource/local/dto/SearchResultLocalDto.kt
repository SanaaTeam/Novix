package com.sanaa.search.dataSource.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "search_result",
    primaryKeys = ["id", "item_id", "item_type"],
    foreignKeys = [
        ForeignKey(
            entity = SearchLocalDto::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SearchResultLocalDto(
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "item_type")
    val itemType: String
) 