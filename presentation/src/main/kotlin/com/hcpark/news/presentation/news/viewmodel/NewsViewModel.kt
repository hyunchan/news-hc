package com.hcpark.news.presentation.news.viewmodel

import androidx.lifecycle.viewModelScope
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlesUseCase
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.news.contract.NewsContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getTopHeadlineArticlesUseCase: GetTopHeadlineArticlesUseCase
) : MVIViewModel<NewsContract.Event, NewsContract.State, NewsContract.Effect>() {

    init {
        fetchTopHeadlines()
    }

    override fun createInitialState(): NewsContract.State {
        return NewsContract.State()
    }

    override fun handleEvent(event: NewsContract.Event) {
        when (event) {
            NewsContract.Event.FetchTopHeadlines -> fetchTopHeadlines()
            is NewsContract.Event.OnArticleClicked -> openLink(event.article)
            is NewsContract.Event.OnBookmarkToggled -> toggleBookmark(event.article)
            is NewsContract.Event.OnShareClicked -> shareLink(event.article)
        }
    }

    private fun fetchTopHeadlines() = viewModelScope.launch {
        getTopHeadlineArticlesUseCase().onSuccess {
            setState { copy(topHeadlines = it) }
        }.onFailure {
            // todo error handling
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