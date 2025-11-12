package com.hcpark.news.presentation.top20.ui

import android.content.Intent
import androidx.compose.foundation.background
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
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.common.ui.ErrorMessageBox
import com.hcpark.news.presentation.common.ui.LoadingProgress
import com.hcpark.news.presentation.common.ui.NewsCard
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
    val entries by remember(state.articles, state.bookmarkedUrls) {
        derivedStateOf {
            state.articles.map { article ->
                NewsCardModel(
                    article,
                    state.bookmarkedUrls.contains(article.url)
                )
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Top 20 Headlines") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                colorScheme.secondary.copy(alpha = 0.8f)
                            )
                        )
                    )
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
        when {
            state.isLoading -> {
                LoadingProgress(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                )
            }

            state.fetchError != null -> {
                ErrorMessageBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    message = state.fetchError.message
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = contentPadding,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(entries) { model ->
                        NewsCard(
                            model = model,
                            onClick = { emitEvent(Event.OnArticleClick(model)) },
                            onSourceClick = { emitEvent(Event.OnSourceClick(model)) },
                            onShare = { emitEvent(Event.OnShareClick(model)) },
                            onBookmark = { emitEvent(Event.OnBookmarkClick(model)) }
                        )
                    }
                }
            }
        }
    }
}
