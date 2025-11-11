package com.hcpark.news.domain.model

data class NewsArticle(
    val source: NewsSource,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: String,
    val content: String
)
