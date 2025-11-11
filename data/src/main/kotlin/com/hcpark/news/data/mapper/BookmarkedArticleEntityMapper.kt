package com.hcpark.news.data.mapper

import com.hcpark.news.data.database.entity.BookmarkedArticleEntity
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.NewsSource
import javax.inject.Inject

class BookmarkedArticleEntityMapper @Inject constructor() {
    operator fun invoke(article: NewsArticle): BookmarkedArticleEntity {
        return BookmarkedArticleEntity(
            url = article.url,
            sourceId = article.source.id,
            sourceName = article.source.name,
            author = article.author,
            title = article.title,
            description = article.description,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt,
            content = article.content
        )
    }

    operator fun invoke(entity: BookmarkedArticleEntity): NewsArticle {
        return NewsArticle(
            url = entity.url,
            source = NewsSource(
                id = entity.sourceId,
                name = entity.sourceName
            ),
            author = entity.author,
            title = entity.title,
            description = entity.description,
            imageUrl = entity.imageUrl,
            publishedAt = entity.publishedAt,
            content = entity.content
        )
    }
}