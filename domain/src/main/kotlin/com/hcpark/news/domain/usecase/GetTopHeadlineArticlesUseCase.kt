package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTopHeadlineArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    suspend operator fun invoke(
        country: Country? = null,
        category: Category? = null,
        sources: String? = null,
        size: Int = 20,
    ) = repository.getTopHeadlineArticles(
        country = country,
        category = category,
        sources = sources,
        size = size
    )
}
