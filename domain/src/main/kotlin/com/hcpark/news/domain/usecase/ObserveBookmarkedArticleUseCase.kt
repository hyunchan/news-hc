package com.hcpark.news.domain.usecase

import androidx.paging.PagingData
import com.hcpark.news.domain.model.BookmarkedArticle
import com.hcpark.news.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ObserveBookmarkedArticleUseCase @Inject constructor(
    private val repository: BookmarkRepository
) {
    operator fun invoke(): Flow<PagingData<BookmarkedArticle>> =
        repository.pagingDataFlow()
}
