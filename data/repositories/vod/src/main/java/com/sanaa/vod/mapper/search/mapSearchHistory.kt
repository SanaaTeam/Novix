package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.search.dto.QueryLocalDto
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import usecase.history.history_param.SearchHistory

fun QueryLocalDto.toEntity(): SearchHistory {
    return SearchHistory(
        id = id,
        query = query,
        timestamp = Instant.fromEpochMilliseconds(this.timestamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}
