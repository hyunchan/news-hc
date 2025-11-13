package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTopHeadlineArticlePagingSourceUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(
        country: Country? = null,
        category: Category? = null,
        sources: String? = null,
    ) = repository.getTopHeadlineArticlePagingSource(
        country = country,
        category = category,
        sources = sources
    )
}
