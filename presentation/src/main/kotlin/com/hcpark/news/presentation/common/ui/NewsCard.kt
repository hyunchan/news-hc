package com.hcpark.news.presentation.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.theme.MyApplicationTheme
import com.hcpark.news.presentation.theme.colorScheme

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    model: NewsCardModel,
    onClick: () -> Unit,
    onSourceClick: () -> Unit,
    onShare: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(
        modifier = modifier
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                var error by remember { mutableStateOf(false) }

                AsyncImage(
                    model = model.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    onLoading = { error = false },
                    onError = { error = true }
                )
                if (error)
                    Icon(
                        modifier = Modifier.size(52.dp),
                        imageVector = Icons.Outlined.ImageNotSupported,
                        contentDescription = "Image Not Supported",
                        tint = colorScheme.secondary,
                    )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Source Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.clickable(onClick = onSourceClick),
                        text = model.sourceName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = model.publishedAt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // Title
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = model.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                // Description
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = model.description,
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
                        Icon(
                            imageVector =
                                if (model.isBookmarked) {
                                    Icons.Filled.Bookmark
                                } else {
                                    Icons.Default.BookmarkBorder
                                },
                            contentDescription = "Bookmark",
                            tint =
                                if (model.isBookmarked) {
                                    colorScheme.primary
                                } else {
                                    Color.Unspecified
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewsCardPreview() {
    var model by remember {
        mutableStateOf(
            NewsCardModel(
                url = "",
                imageUrl = "",
                sourceId = null,
                sourceName = "Sample Source",
                title = "Sample Title",
                description = "Sample Description",
                publishedAt = "a moment ago",
                isBookmarked = true
            )
        )
    }
    MyApplicationTheme {
        NewsCard(
            model = model,
            onClick = { },
            onSourceClick = { },
            onShare = { },
            onBookmark = {
                model = model.copy(isBookmarked = !model.isBookmarked)
            }
        )
    }
}
