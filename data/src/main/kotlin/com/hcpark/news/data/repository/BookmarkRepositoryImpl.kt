package com.hcpark.news.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hcpark.news.data.database.dao.BookmarkedArticleDao
import com.hcpark.news.data.mapper.BookmarkedArticleEntityMapper
import com.hcpark.news.data.remote.util.callCatching
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val articleDao: BookmarkedArticleDao,
    private val bookmarkedArticleEntityMapper: BookmarkedArticleEntityMapper
) : BookmarkRepository {
    override suspend fun insert(newsArticle: NewsArticle): Result<Unit> =
        callCatching {
            articleDao.insert(bookmarkedArticleEntityMapper(newsArticle))
        }

    override suspend fun delete(newsArticle: NewsArticle): Result<Unit> =
        callCatching {
            articleDao.delete(bookmarkedArticleEntityMapper(newsArticle))
        }


    override fun observeNewsArticlePagingData(): Flow<PagingData<NewsArticle>> =
        Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { articleDao.pagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                bookmarkedArticleEntityMapper(entity)
            }
        }

    override suspend fun exists(url: String): Result<Boolean> =
        callCatching {
            articleDao.exists(url)
        }


    override suspend fun clearAll(): Result<Unit> =
        callCatching {
            articleDao.clearAll()
        }
}