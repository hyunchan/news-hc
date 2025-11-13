package com.hcpark.news.presentation.news.ui

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.hcpark.news.domain.model.Category
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.common.ui.ErrorMessageBox
import com.hcpark.news.presentation.common.ui.LoadingProgress
import com.hcpark.news.presentation.common.ui.NewsCard
import com.hcpark.news.presentation.news.contract.NewsContract.Effect
import com.hcpark.news.presentation.news.contract.NewsContract.Event
import com.hcpark.news.presentation.news.contract.NewsContract.ModalState
import com.hcpark.news.presentation.news.contract.NewsContract.State
import com.hcpark.news.presentation.news.viewmodel.NewsViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    navigate: (String) -> Unit,
    navigateToMain: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val emitEvent = viewModel::setEvent
    val lazyPagingArticle = viewModel.topHeadlinePagingDataFlow.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current

    fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    NewsScreenContent(
        state = state,
        lazyPagingArticle = lazyPagingArticle,
        snackbarHostState = snackbarHostState,
        emitEvent = viewModel::setEvent
    )

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is Effect.Launch -> navigate(effect.route)
                is Effect.Toast -> snackbarHostState.showSnackbar(effect.message)
                is Effect.LaunchIntent -> startActivity(effect.intent)
                Effect.NavigateToMain -> navigateToMain()
            }
        }
    }
    val onDismiss: () -> Unit = { emitEvent(Event.OnModalDismiss) }
    when (state.modalState) {
        is ModalState.None -> Unit
        ModalState.ConfirmReturnToMain -> {
            ConfirmReturnToMainDialog(
                onConfirm = { emitEvent(Event.OnReturnToMainConfirm) },
                onDismissRequest = onDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreenContent(
    state: State,
    lazyPagingArticle: LazyPagingItems<NewsCardModel>,
    snackbarHostState: SnackbarHostState,
    emitEvent: (Event) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("News") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { emitEvent(Event.OnReturnToMainClick) },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "return to main"
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            if (state.filterVisible) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Category.entries) { category ->
                        FilterChip(
                            selected = state.category == category,
                            onClick = { emitEvent(Event.OnCategoryChange(category)) },
                            label = { Text(text = category.name) }
                        )
                    }
                }
            }

            when (val refreshState = lazyPagingArticle.loadState.refresh) {
                is LoadState.NotLoading -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(count = lazyPagingArticle.itemCount) { position ->
                            lazyPagingArticle[position]?.let { model ->
                                NewsCard(
                                    model = model,
                                    onClick = { emitEvent(Event.OnArticleClick(model)) },
                                    onSourceClick = { emitEvent(Event.OnSourceClick(model)) },
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
                }

                is LoadState.Loading -> {
                    LoadingProgress(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                    )
                }

                is LoadState.Error -> {
                    ErrorMessageBox(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                        refreshState.error.message
                    )
                }
            }
        }
    }
}