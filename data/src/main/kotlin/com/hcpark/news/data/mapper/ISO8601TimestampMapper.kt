package com.hcpark.news.data.mapper

import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ISO8601TimestampMapper @Inject constructor() {
    // ISO 8601 format: "2025-10-28T14:30:00Z"
    operator fun invoke(timestamp: String): Instant {
        return Instant.parse(timestamp)
    }
}
