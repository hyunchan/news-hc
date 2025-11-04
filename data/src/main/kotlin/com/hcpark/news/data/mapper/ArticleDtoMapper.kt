package com.hcpark.news.data.mapper

import com.hcpark.news.data.remote.dto.ArticleDto
import com.hcpark.news.domain.model.NewsArticle
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDtoMapper @Inject constructor(
    private val sourceDtoMapper: SourceDtoMapper,
    private val iso8601TimestampMapper: ISO8601TimestampMapper
) {
    operator fun invoke(dto: ArticleDto): NewsArticle {
        return NewsArticle(
            source = dto.source(),
            author = dto.author ?: "",
            title = dto.title,
            description = dto.description ?: "",
            url = dto.url,
            imageUrl = dto.urlToImage ?: "",
            publishedAt = dto.publishedAt(),
            content = dto.content ?: ""
        )
    }

    private fun ArticleDto.source() =
        sourceDtoMapper(source)

    private fun ArticleDto.publishedAt(): LocalDateTime {
        return iso8601TimestampMapper(publishedAt)
            .let { LocalDateTime.ofInstant(it, ZoneId.systemDefault()) }
    }
}
