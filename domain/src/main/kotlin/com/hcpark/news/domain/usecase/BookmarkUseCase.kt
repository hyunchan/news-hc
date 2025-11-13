package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.BookmarkedArticle
import com.hcpark.news.domain.repository.BookmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(
        url: String,
        title: String,
        description: String,
        sourceId: String?,
        sourceName: String,
        publishedAt: String,
        imageUrl: String
    ) = repository.insert(
        BookmarkedArticle(
            url = url,
            title = title,
            description = description,
            sourceId = sourceId,
            sourceName = sourceName,
            publishedAt = publishedAt,
            imageUrl = imageUrl
        )
    )
}
