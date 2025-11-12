package com.hcpark.news.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hcpark.news.data.database.dao.BookmarkedArticleDao
import com.hcpark.news.data.database.entity.BookmarkedArticleEntity

@Database(
    entities = [BookmarkedArticleEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkedArticleDao(): BookmarkedArticleDao
}
