package com.hcpark.news.presentation.news.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.NewsSource
import com.hcpark.news.presentation.theme.MyApplicationTheme
import com.hcpark.news.presentation.theme.colorScheme
import java.time.LocalDateTime

@Composable
fun NewsCard(
    article: NewsArticle,
    onClick: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Hero Image
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // Source Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.source.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = article.publishedAt.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Title
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                // Description
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = article.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onShare) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                    IconButton(onClick = onBookmark) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewsCardPreview() {
    val article = NewsArticle(
        source = NewsSource("", "Sample Source"),
        author = "Sample Author",
        title = "Sample Title",
        description = "Sample Description",
        url = "",
        imageUrl = "",
        publishedAt = LocalDateTime.now(),
        content = "Sample Content"
    )
    MyApplicationTheme {
        NewsCard(
            article = article,
            onClick = { },
            onShare = { },
            onBookmark = { }
        )
    }
}