package com.hcpark.news.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EverythingResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)
