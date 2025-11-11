package com.hcpark.news.domain.repository

import androidx.paging.PagingData
import com.hcpark.news.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun insert(newsArticle: NewsArticle): Result<Unit>

    suspend fun delete(newsArticle: NewsArticle): Result<Unit>

    fun observeNewsArticlePagingData(): Flow<PagingData<NewsArticle>>

    suspend fun exists(url: String): Result<Boolean>

    suspend fun clearAll(): Result<Unit>
}
