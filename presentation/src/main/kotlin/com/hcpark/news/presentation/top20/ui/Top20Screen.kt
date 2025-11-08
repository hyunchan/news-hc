package com.hcpark.news.presentation.top20.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.hcpark.news.presentation.news.ui.NewsCard
import com.hcpark.news.presentation.top20.contract.Top20Contract.Effect
import com.hcpark.news.presentation.top20.contract.Top20Contract.Event
import com.hcpark.news.presentation.top20.contract.Top20Contract.ModalState
import com.hcpark.news.presentation.top20.contract.Top20Contract.State
import com.hcpark.news.presentation.top20.viewmodel.Top20ViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun Top20Screen(
    viewModel: Top20ViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val emitEvent: (Event) -> Unit = viewModel::setEvent

    ViewScreenContent(
        state = state,
        emitEvent = emitEvent,
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Launch -> navigate(effect.route)
            }
        }
    }

    when (state.modalState) {
        is ModalState.Dismiss -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewScreenContent(
    state: State,
    emitEvent: (Event) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Top 20 Headlines") })
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { emitEvent(Event.OnMoreNewsClick) },
                    shape = RoundedCornerShape(100)
                ) {
                    Text(text = "More News")
                }
            }
        }
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.articles) { article ->
                NewsCard(
                    article = article,
                    onClick = { emitEvent(Event.OnArticleClick(article)) },
                    onShare = { emitEvent(Event.OnShareClick(article)) },
                    onBookmark = { emitEvent(Event.OnBookmarkClick(article)) }
                )
            }
        }
    }
}
