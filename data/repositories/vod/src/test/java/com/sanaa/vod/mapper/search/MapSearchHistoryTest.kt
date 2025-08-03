package com.sanaa.vod.mapper.search

import com.sanaa.vod.dataSource.local.history.dto.search.QueryLocalDto
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import usecase.history.history_param.SearchHistory

class MapSearchHistoryTest {

    @Test
    fun `toEntity maps QueryLocalDto to SearchHistory`() {
        val queryLocalDto = QueryLocalDto(
            id = 1,
            query = "query",
            timestamp = 1234567890L
        )
        val expectedSearchHistory = SearchHistory(
            id = 1,
            query = "query",
            timestamp = Instant.Companion.fromEpochMilliseconds(1234567890L)
                .toLocalDateTime(TimeZone.Companion.currentSystemDefault())
        )
        Assertions.assertEquals(expectedSearchHistory, queryLocalDto.toEntity())
    }
}