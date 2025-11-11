package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.Language
import com.hcpark.news.domain.model.SearchIn
import com.hcpark.news.domain.model.SortBy
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchArticlesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        query: String,
        searchIns: List<SearchIn> = SearchIn.Defaults,
        langauge: Language = Language.Default,
        sortBy: SortBy = SortBy.Default,
        size: Int = 20
    ) = repository.search(
        query = query,
        searchIns = searchIns,
        language = langauge,
        sortBy = sortBy,
        size = size
    )
}
