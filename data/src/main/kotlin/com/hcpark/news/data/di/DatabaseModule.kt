package com.hcpark.news.data.di

import android.content.Context
import androidx.room.Room
import com.hcpark.news.data.database.AppDatabase
import com.hcpark.news.data.database.dao.BookmarkedArticleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "news-hc-database"
        ).fallbackToDestructiveMigration(dropAllTables = true).build()
    }

    @Provides
    @Singleton
    fun provideBookmarkedArticleDao(appDatabase: AppDatabase): BookmarkedArticleDao {
        return appDatabase.bookmarkedArticleDao()
    }
}
