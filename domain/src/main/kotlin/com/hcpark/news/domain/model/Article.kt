package com.hcpark.news.domain.model

import java.time.LocalDateTime

data class NewsArticle(
    val source: NewsSource,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val publishedAt: LocalDateTime?,
    val content: String
)
