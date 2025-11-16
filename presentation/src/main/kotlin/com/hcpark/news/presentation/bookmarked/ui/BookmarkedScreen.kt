package com.hcpark.news.presentation.bookmarked.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Effect
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Event
import com.hcpark.news.presentation.bookmarked.viewmodel.BookmarkedViewModel
import com.hcpark.news.presentation.common.model.NewsCardEvent
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.common.ui.LoadingProgress
import com.hcpark.news.presentation.common.ui.MessageBox
import com.hcpark.news.presentation.common.ui.NewsCard
import com.hcpark.news.presentation.theme.colorScheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookmarkedScreen(
    viewModel: BookmarkedViewModel = hiltViewModel(),
    navigate: (String) -> Unit
) {
    // val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingItems = viewModel.newsCardModelPagingData.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    BookmarkedScreenContent(
        lazyPagingItems = lazyPagingItems,
        emitEvent = viewModel::setEvent
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Launch -> navigate(effect.route)
                is Effect.Toast -> snackbarHostState.showSnackbar(effect.message)
                is Effect.LaunchIntent -> startActivity(effect.intent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkedScreenContent(
    lazyPagingItems: LazyPagingItems<NewsCardModel>,
    emitEvent: (Event) -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(title = { Text("Bookmarked") })
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val refreshState = lazyPagingItems.loadState.refresh

            BookmarkedScreenCardList(lazyPagingItems, emitEvent, refreshState)
        }
    }

    if (lazyPagingItems.loadState.refresh is LoadState.Loading) {
        LoadingProgress(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun BookmarkedScreenCardList(
    lazyPagingItems: LazyPagingItems<NewsCardModel>,
    emitEvent: (Event) -> Unit,
    refreshState: LoadState = lazyPagingItems.loadState.refresh,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (refreshState is LoadState.Error) {
            item {
                MessageBox(
                    modifier = Modifier.fillMaxSize(),
                    refreshState.error.message
                )
            }
        } else if (lazyPagingItems.itemCount == 0) {
            item {
                BookmarkedScreenEmptyMessage()
            }
        }
        items(
            count = lazyPagingItems.itemCount,
            key = { lazyPagingItems[it]?.url ?: Unit }
        ) { position ->
            lazyPagingItems[position]?.let { model ->
                NewsCard(
                    modifier = Modifier.animateItem(),
                    model = model,
                    onEvent = {
                        when (it) {
                            NewsCardEvent.BookmarkClick -> emitEvent(
                                Event.OnBookmarkClick(
                                    model
                                )
                            )

                            NewsCardEvent.CardClick -> emitEvent(Event.OnArticleClick(model))
                            NewsCardEvent.ShareClick -> emitEvent(Event.OnShareClick(model))
                            NewsCardEvent.SourceClick -> Unit
                        }
                    }
                )
            }
        }

        if (lazyPagingItems.loadState.append is LoadState.Loading) {
            item {
                LoadingProgress(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BookmarkedScreenEmptyMessage() {
    MessageBox(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp),
        message = "북마크가 없어요!"
    ) { defaultMessage ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = Icons.Outlined.Bookmarks,
                contentDescription = "no bookmarks",
                tint = colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
            )
            defaultMessage()
        }
    }
}
