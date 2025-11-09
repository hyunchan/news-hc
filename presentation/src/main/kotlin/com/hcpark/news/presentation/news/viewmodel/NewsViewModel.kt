package com.hcpark.news.presentation.news.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.NewsSource
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlePagingSourceUseCase
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.main.navigation.MainRoute
import com.hcpark.news.presentation.news.contract.NewsContract.Effect
import com.hcpark.news.presentation.news.contract.NewsContract.Event
import com.hcpark.news.presentation.news.contract.NewsContract.State
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
) : MVIViewModel<Event, State, Effect>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val topHeadlinePagingDataFlow = uiState
        .map { it.category to it.sources }
        .distinctUntilChanged()
        .flatMapLatest { (category, sources) ->
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    getTopHeadlineArticlePagingSourceUseCase(
                        category = category,
                        sources = sources
                    )
                }
            ).flow
        }.cachedIn(viewModelScope)

    init {
        init(savedStateHandle)
    }

    private fun init(savedStateHandle: SavedStateHandle) {
        val category = savedStateHandle.get<String>(MainRoute.CATEGORY)
            .let(Category::fromKey)
        val sources = savedStateHandle.get<String>(MainRoute.SOURCES)
            ?.takeIf { it != "null" }
        setState { copy(category = category, sources = sources) }
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnArticleClick -> openLink(event.article)
            is Event.OnSourceClick -> launchNews(event.source)
            is Event.OnBookmarkClick -> toggleBookmark(event.article)
            is Event.OnShareClick -> shareLink(event.article)
            is Event.OnCategoryChange -> updateCategory(event.category)
        }
    }

    private fun openLink(article: NewsArticle) {
        //todo
    }

    private fun launchNews(source: NewsSource) {
        setEffect(Effect.Launch(MainRoute.news(sources = source.id)))
    }

    private fun toggleBookmark(article: NewsArticle) {
        //todo
    }

    private fun shareLink(article: NewsArticle) {
        //todo
    }

    private fun updateCategory(category: Category) {
        setState { copy(category = category) }
    }
}