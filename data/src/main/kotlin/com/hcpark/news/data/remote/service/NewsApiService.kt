package com.hcpark.news.data.remote.service

import com.hcpark.news.data.remote.dto.EverythingResponse
import com.hcpark.news.data.remote.dto.TopHeadlinesResponse
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.model.Language
import com.hcpark.news.domain.model.SearchIn
import com.hcpark.news.domain.model.SortBy
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class NewsApiService @Inject constructor(
    private val client: HttpClient
) {
    /**
     * @param country 국가 코드 (기본값: 미국), 무료 버전에서 기본값만 지원
     */
    suspend fun getTopHeadlines(
        country: Country = Country.Default,
        category: Category = Category.Default,
        page: Int = 1,
        pageSize: Int = 20
    ): TopHeadlinesResponse {
        return client.get("v2/top-headlines") {
            parameter("country", country.key)
            category.key?.let { parameter("category", it) }
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }

    @Suppress("LongParameterList")
    suspend fun getEverything(
        query: String,
        searchIns: List<SearchIn> = SearchIn.Defaults,
        language: Language = Language.Default,
        sortBy: SortBy = SortBy.Default,
        page: Int = 1,
        pageSize: Int = 20
    ): EverythingResponse {
        return client.get("v2/everything") {
            parameter("q", query)
            parameter("searchIn", searchIns.joinToString(",") { it.key })
            parameter("language", language.key)
            parameter("sortBy", sortBy.key)
            parameter("page", page)
            parameter("pageSize", pageSize)
        }.body()
    }
}
