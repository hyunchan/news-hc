package com.hcpark.news.presentation.bookmarked.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Effect
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Event
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.State
import com.hcpark.news.presentation.bookmarked.viewmodel.BookmarkedViewModel
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.common.ui.ErrorMessageBox
import com.hcpark.news.presentation.common.ui.LoadingProgress
import com.hcpark.news.presentation.common.ui.NewsCard
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BookmarkedScreen(
    viewModel: BookmarkedViewModel = hiltViewModel(),
    navigate: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingArticle = viewModel.articlePagingDataFlow.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    BookmarkedScreenContent(
        state = state,
        lazyPagingArticle = lazyPagingArticle,
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
    state: State,
    lazyPagingArticle: LazyPagingItems<NewsCardModel>,
    emitEvent: (Event) -> Unit
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(title = { Text("Bookmarked") })
        }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            val refreshState = lazyPagingArticle.loadState.refresh

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (refreshState is LoadState.Error) {
                    item {
                        ErrorMessageBox(
                            modifier = Modifier.fillMaxSize(),
                            refreshState.error.message
                        )
                    }
                }
                items(
                    count = lazyPagingArticle.itemCount,
                    key = { lazyPagingArticle[it]?.url ?: Unit }
                ) { position ->
                    lazyPagingArticle[position]?.let { model ->
                        NewsCard(
                            modifier = Modifier.animateItem(),
                            model = model,
                            onClick = { emitEvent(Event.OnArticleClick(model)) },
                            onSourceClick = { },
                            onShare = { emitEvent(Event.OnShareClick(model)) },
                            onBookmark = { emitEvent(Event.OnBookmarkClick(model)) }
                        )
                    }
                }

                if (lazyPagingArticle.loadState.append is LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (refreshState is LoadState.Loading) {
                LoadingProgress(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                )
            }
        }
    }
}
