package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlineArticlePagingSourceUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(
        category: Category = Category.Default
    ) = repository.getTopHeadlineArticlePagingSource(category)
}