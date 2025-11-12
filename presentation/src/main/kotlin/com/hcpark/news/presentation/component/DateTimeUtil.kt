package com.hcpark.news.presentation.component

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object DateTimeUtil {
    fun parse(timestamp: String): Instant {
        return Instant.parse(timestamp)
    }

    fun localDateTime(timestamp: String): LocalDateTime {
        return parse(timestamp)
            .let { LocalDateTime.ofInstant(parse(timestamp), ZoneId.systemDefault()) }
    }
}
