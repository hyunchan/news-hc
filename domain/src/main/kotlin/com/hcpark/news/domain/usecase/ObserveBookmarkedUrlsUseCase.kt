package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.repository.BookmarkRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveBookmarkedUrlsUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke() = repository.urls()
}
