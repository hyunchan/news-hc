package com.hcpark.news.presentation.news.ui

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.news.contract.NewsContract
import com.hcpark.news.presentation.news.contract.NewsContract.Event
import com.hcpark.news.presentation.news.contract.NewsContract.State
import com.hcpark.news.presentation.news.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lazyPagingArticle = viewModel.topHeadlinePagingDataFlow.collectAsLazyPagingItems()

    NewsScreenContent(
        state = state,
        lazyPagingArticle = lazyPagingArticle,
        emitEvent = viewModel::setEvent
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            //todo
        }
    }

    when (state.modalState) {
        NewsContract.ModalState.Dismiss -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreenContent(
    state: State,
    lazyPagingArticle: LazyPagingItems<NewsArticle>,
    emitEvent: (Event) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("News") })
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(count = lazyPagingArticle.itemCount) { position ->
                lazyPagingArticle[position]?.let {
                    NewsCard(
                        article = it,
                        onClick = { emitEvent(Event.OnArticleClick(it)) },
                        onShare = { emitEvent(Event.OnShareClick(it)) },
                        onBookmark = { emitEvent(Event.OnBookmarkClick(it)) }
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

        if (lazyPagingArticle.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}