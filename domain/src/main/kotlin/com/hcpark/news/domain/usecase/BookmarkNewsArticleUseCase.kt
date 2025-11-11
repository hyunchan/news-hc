package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.repository.BookmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkNewsArticleUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(newsArticle: NewsArticle) =
        repository.insert(newsArticle)
}