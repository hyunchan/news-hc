package com.hcpark.news.presentation.top20.ui

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hcpark.news.presentation.common.model.NewsCardEvent
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.common.ui.LoadingProgress
import com.hcpark.news.presentation.common.ui.MessageBox
import com.hcpark.news.presentation.common.ui.NewsCard
import com.hcpark.news.presentation.extension.isEmpty
import com.hcpark.news.presentation.extension.isLastItemFullyVisible
import com.hcpark.news.presentation.theme.colorScheme
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
    val snackbarHostState = remember { SnackbarHostState() }
    val emitEvent: (Event) -> Unit = viewModel::setEvent

    val context = LocalContext.current

    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    Top20ScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        emitEvent = emitEvent,
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

    when (state.modalState) {
        is ModalState.Dismiss -> Unit
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Top20ScreenContent(
    state: State,
    snackbarHostState: SnackbarHostState,
    emitEvent: (Event) -> Unit,
) {
    val listState = rememberLazyListState()
    val listScrollable by rememberScrollable(listState)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Top 20 Headlines") },
                actions = {
                    IconButton(onClick = { emitEvent(Event.OnBookmarkedListClick) }) {
                        Icon(
                            imageVector = Icons.Outlined.Bookmarks,
                            contentDescription = "Bookmarked List"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Top20ScreenMoreNewsButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .bottomBarBackground(scrollable = listScrollable),
                onClick = { emitEvent(Event.OnMoreNewsClick) }
            )
        }
    ) { contentPadding ->
        Top20ScreenNewsList(listState, contentPadding, state, emitEvent)
    }
    if (state.isLoading) {
        LoadingProgress(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun rememberScrollable(lazyListState: LazyListState) =
    remember(lazyListState) {
        derivedStateOf {
            if (lazyListState.isEmpty()) {
                false
            } else {
                !lazyListState.isLastItemFullyVisible()
            }
        }
    }

@Composable
private fun Top20ScreenNewsList(
    lazyListState: LazyListState,
    contentPadding: PaddingValues,
    state: State,
    emitEvent: (Event) -> Unit,
) {
    val entries by remember(state.articles, state.bookmarkedUrls) {
        derivedStateOf {
            state.articles.map { article ->
                val isBookmarked = state.bookmarkedUrls.contains(article.url)
                NewsCardModel(article, isBookmarked)
            }
        }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.fetchError != null) {
            item {
                MessageBox(
                    modifier = Modifier.fillMaxSize(),
                    message = state.fetchError.message
                )
            }
        }
        items(entries) { model ->
            NewsCard(
                model = model,
                onEvent = {
                    when (it) {
                        is NewsCardEvent.BookmarkClick -> emitEvent(Event.OnBookmarkClick(model))
                        is NewsCardEvent.CardClick -> emitEvent(Event.OnArticleClick(model))
                        is NewsCardEvent.SourceClick -> emitEvent(Event.OnSourceClick(model))
                        is NewsCardEvent.ShareClick -> emitEvent(Event.OnShareClick(model))
                    }
                }
            )
        }
    }
}

@Composable
private fun Top20ScreenMoreNewsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .navigationBarsPadding()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text = "More News")
        }
    }
}

@Composable
private fun Modifier.bottomBarBackground(
    scrollable: Boolean
): Modifier {
    val bottomBarAlpha by animateFloatAsState(
        targetValue = if (scrollable) 0.8f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "bottomBarAlpha"
    )
    return background(
        brush = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                colorScheme.secondary.copy(alpha = bottomBarAlpha)
            )
        )
    )
}
