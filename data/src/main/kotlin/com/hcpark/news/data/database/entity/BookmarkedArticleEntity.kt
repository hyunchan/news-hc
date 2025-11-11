package com.hcpark.news.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarked_articles")
data class BookmarkedArticleEntity(
    @PrimaryKey
    val url: String,
    val sourceId: String?,
    val sourceName: String,
    val author: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String
)
