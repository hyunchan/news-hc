package com.hcpark.news.presentation.news.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcpark.news.presentation.news.contract.NewsContract.Event
import com.hcpark.news.presentation.news.contract.NewsContract.State
import com.hcpark.news.presentation.news.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    NewsScreenContent(
        state = state,
        emitEvent = viewModel::setEvent
    )
    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            //todo
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreenContent(
    state: State,
    emitEvent: (Event) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("News") }
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.topHeadlines) { article ->
                NewsCard(
                    article = article,
                    onClick = { emitEvent(Event.OnArticleClicked(article)) },
                    onShare = { emitEvent(Event.OnShareClicked(article)) },
                    onBookmark = { emitEvent(Event.OnBookmarkToggled(article)) }
                )
            }
        }
    }
}