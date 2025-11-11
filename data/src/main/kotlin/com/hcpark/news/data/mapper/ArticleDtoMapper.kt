package com.hcpark.news.data.mapper

import com.hcpark.news.data.remote.dto.ArticleDto
import com.hcpark.news.domain.model.NewsArticle
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleDtoMapper @Inject constructor(
    private val sourceDtoMapper: SourceDtoMapper
) {
    operator fun invoke(dto: ArticleDto): NewsArticle {
        return NewsArticle(
            source = dto.source(),
            author = dto.author ?: "",
            title = dto.title,
            description = dto.description ?: "",
            url = dto.url,
            imageUrl = dto.urlToImage ?: "",
            publishedAt = dto.publishedAt,
            content = dto.content ?: ""
        )
    }

    private fun ArticleDto.source() =
        sourceDtoMapper(source)
}
