package com.hcpark.news.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArticleDto(
    val source: SourceDto,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String, // ISO 8601 format: "2025-10-28T14:30:00Z"
    val content: String?
)
