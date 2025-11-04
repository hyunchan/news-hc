package com.hcpark.news.data.mapper

import com.hcpark.news.data.remote.dto.SourceDto
import com.hcpark.news.domain.model.NewsSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceDtoMapper @Inject constructor() {
    operator fun invoke(dto: SourceDto): NewsSource {
        return NewsSource(
            id = dto.id,
            name = dto.name
        )
    }
}
