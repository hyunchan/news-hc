package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.BookmarkedArticle
import com.hcpark.news.domain.repository.BookmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(bookmarkedArticle: BookmarkedArticle) =
        repository.insert(bookmarkedArticle)
}
