package com.hcpark.news.domain.repository

import androidx.paging.PagingSource
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.model.Language
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.SearchIn
import com.hcpark.news.domain.model.SortBy

interface NewsRepository {
    suspend fun getTopHeadlineArticles(
        country: Country? = null,
        category: Category? = null,
        sources: String? = null,
        size: Int = 20
    ): Result<List<NewsArticle>>

    fun getTopHeadlineArticlePagingSource(
        country: Country? = null,
        category: Category? = null,
        sources: String? = null,
    ): PagingSource<Int, NewsArticle>

    suspend fun search(
        query: String,
        searchIns: List<SearchIn> = SearchIn.Defaults,
        language: Language = Language.Default,
        sortBy: SortBy = SortBy.Default,
        size: Int = 20
    ): Result<List<NewsArticle>>
}
