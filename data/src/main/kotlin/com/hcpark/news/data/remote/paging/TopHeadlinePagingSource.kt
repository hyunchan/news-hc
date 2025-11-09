package com.hcpark.news.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hcpark.news.data.mapper.ArticleDtoMapper
import com.hcpark.news.data.remote.service.NewsApiService
import com.hcpark.news.data.remote.util.callCatching
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.NewsArticle

class TopHeadlinePagingSource(
    private val apiService: NewsApiService,
    private val articleDtoMapper: ArticleDtoMapper,
    private val category: Category,
    val pageSize: Int = 20
) : PagingSource<Int, NewsArticle>() {
    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        return callCatching {
            val page = params.key ?: 1
            val response = apiService.getTopHeadlines(
                page = page,
                pageSize = pageSize,
                category = category
            )

            val newsArticles = response.articles
                .map(articleDtoMapper::invoke)

            LoadResult.Page(
                data = newsArticles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (newsArticles.isEmpty()) null else page + 1
            )
        }.recover {
            LoadResult.Error(it)
        }.getOrNull() ?: LoadResult.Error(Exception("callCatching failed"))
    }
}
