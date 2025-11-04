package com.hcpark.news.data.repository

import com.hcpark.news.data.mapper.ArticleDtoMapper
import com.hcpark.news.data.remote.service.NewsApiService
import com.hcpark.news.data.remote.util.callCatching
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Language
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.SearchIn
import com.hcpark.news.domain.model.SortBy
import com.hcpark.news.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    private val articleDtoMapper: ArticleDtoMapper
) : NewsRepository {
    override suspend fun getTopHeadlineArticles(
        category: Category,
        size: Int
    ): Result<List<NewsArticle>> = callCatching {
        apiService.getTopHeadlines(category = category, pageSize = size)
    }.mapCatching {
        it.articles.map(articleDtoMapper::invoke)
    }

    override suspend fun search(
        query: String,
        searchIns: List<SearchIn>,
        language: Language,
        sortBy: SortBy,
        size: Int
    ): Result<List<NewsArticle>> = callCatching {
        apiService.getEverything(
            query = query,
            searchIns = searchIns,
            language = language,
            sortBy = sortBy,
            pageSize = size
        )
    }.mapCatching {
        it.articles.map(articleDtoMapper::invoke)
    }
}
