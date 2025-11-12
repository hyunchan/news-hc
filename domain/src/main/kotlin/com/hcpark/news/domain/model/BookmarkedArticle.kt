package com.hcpark.news.domain.model

data class BookmarkedArticle(
    val url: String,
    val title: String,
    val description: String,
    val sourceId: String?,
    val sourceName: String,
    val publishedAt: String,
    val imageUrl: String
)