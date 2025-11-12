package com.hcpark.news.domain.repository

import androidx.paging.PagingData
import com.hcpark.news.domain.model.BookmarkedArticle
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun insert(article: BookmarkedArticle): Result<Unit>

    suspend fun delete(url: String): Result<Unit>

    fun pagingDataFlow(): Flow<PagingData<BookmarkedArticle>>

    suspend fun exists(url: String): Result<Boolean>

    fun urls(): Flow<Set<String>>

    suspend fun clearAll(): Result<Unit>
}
