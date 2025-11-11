package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.repository.BookmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClearAllBookmarkedNewsArticleUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    suspend operator fun invoke(): Result<Unit> =
        repository.clearAll()
}
