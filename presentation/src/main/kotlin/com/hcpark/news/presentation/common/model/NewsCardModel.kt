package com.hcpark.news.presentation.common.model

import android.content.Intent
import android.net.Uri
import com.hcpark.news.domain.model.BookmarkedArticle
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.component.DateTimeUtil
import java.time.format.DateTimeFormatter

data class NewsCardModel(
    val url: String,
    val imageUrl: String,
    val sourceId: String?,
    val sourceName: String,
    val publishedAt: String,
    val title: String,
    val description: String,
    val isBookmarked: Boolean
) {
    constructor(
        newsArticle: NewsArticle,
        isBookmarked: Boolean
    ) : this(
        url = newsArticle.url,
        imageUrl = newsArticle.imageUrl,
        sourceId = newsArticle.source.id,
        sourceName = newsArticle.source.name,
        publishedAt = newsArticle.formattedPublishedAt(),
        title = newsArticle.title,
        description = newsArticle.description,
        isBookmarked = isBookmarked
    )

    constructor(
        bookmarkedArticle: BookmarkedArticle
    ) : this(
        url = bookmarkedArticle.url,
        imageUrl = bookmarkedArticle.imageUrl,
        sourceId = bookmarkedArticle.sourceId,
        sourceName = bookmarkedArticle.sourceName,
        publishedAt = bookmarkedArticle.publishedAt,
        title = bookmarkedArticle.title,
        description = bookmarkedArticle.description,
        isBookmarked = true
    )

    fun viewIntent(): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
    }

    fun shareIntent(): Intent {
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }.let {
            Intent.createChooser(it, title)
        }
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        private fun NewsArticle.formattedPublishedAt(): String {
            return try {
                DateTimeUtil.localDateTime(publishedAt)
                    .let { dateTimeFormatter.format(it) }
            } catch (_: Exception) {
                publishedAt
            }
        }
    }
}
