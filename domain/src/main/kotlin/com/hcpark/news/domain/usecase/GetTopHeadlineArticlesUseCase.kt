package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlineArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(
        category: Category = Category.Default,
        size: Int = 20,
    ) = repository.getTopHeadlineArticles(
        category = category,
        size = size
    )
}
