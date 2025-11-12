package com.hcpark.news.data.mapper

import com.hcpark.news.data.database.entity.BookmarkedArticleEntity
import com.hcpark.news.domain.model.BookmarkedArticle
import javax.inject.Inject

class BookmarkedArticleEntityMapper @Inject constructor() {
    operator fun invoke(article: BookmarkedArticle): BookmarkedArticleEntity {
        return BookmarkedArticleEntity(
            url = article.url,
            title = article.title,
            description = article.description,
            sourceId = article.sourceId,
            sourceName = article.sourceName,
            imageUrl = article.imageUrl,
            publishedAt = article.publishedAt
        )
    }

    operator fun invoke(entity: BookmarkedArticleEntity): BookmarkedArticle {
        return BookmarkedArticle(
            url = entity.url,
            title = entity.title,
            description = entity.description,
            sourceId = entity.sourceId,
            sourceName = entity.sourceName,
            imageUrl = entity.imageUrl,
            publishedAt = entity.publishedAt
        )
    }
}