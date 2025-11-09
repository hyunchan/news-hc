package com.hcpark.news.presentation.news.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlePagingSourceUseCase
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.main.navigation.MainRoute
import com.hcpark.news.presentation.news.contract.NewsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTopHeadlineArticlePagingSourceUseCase: GetTopHeadlineArticlePagingSourceUseCase
) : MVIViewModel<NewsContract.Event, NewsContract.State, NewsContract.Effect>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val topHeadlinePagingDataFlow = uiState.map { it.category }
        .distinctUntilChanged()
        .flatMapLatest { category ->
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    getTopHeadlineArticlePagingSourceUseCase(category = category)
                }
            ).flow
        }.cachedIn(viewModelScope)

    init {
        init(savedStateHandle)
    }

    private fun init(savedStateHandle: SavedStateHandle) {
        val category = savedStateHandle.get<String>(MainRoute.CATEGORY)
            .let(Category::fromKey)
        setState { copy(category = category) }
    }

    override fun createInitialState(): NewsContract.State {
        return NewsContract.State()
    }

    override fun handleEvent(event: NewsContract.Event) {
        when (event) {
            is NewsContract.Event.OnArticleClick -> openLink(event.article)
            is NewsContract.Event.OnBookmarkClick -> toggleBookmark(event.article)
            is NewsContract.Event.OnShareClick -> shareLink(event.article)
        }
    }

    private fun openLink(article: NewsArticle) {
        //todo
    }

    private fun toggleBookmark(article: NewsArticle) {
        //todo
    }

    private fun shareLink(article: NewsArticle) {
        //todo
    }
}