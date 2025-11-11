package com.hcpark.news.data.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hcpark.news.data.database.entity.BookmarkedArticleEntity

@Dao
interface BookmarkedArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: BookmarkedArticleEntity)

    @Delete
    suspend fun delete(article: BookmarkedArticleEntity)

    @Query("SELECT * FROM bookmarked_articles")
    fun pagingSource(): PagingSource<Int, BookmarkedArticleEntity>

    @Query("SELECT EXISTS(SELECT * FROM bookmarked_articles WHERE url = :url)")
    suspend fun exists(url: String): Boolean

    @Query("DELETE FROM bookmarked_articles")
    suspend fun clearAll()
}
