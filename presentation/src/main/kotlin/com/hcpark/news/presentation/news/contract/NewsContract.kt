package com.hcpark.news.presentation.news.contract

import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object NewsContract {
    data class State(
        val topHeadlines: List<NewsArticle> = emptyList()
    ) : UiState

    sealed interface Event : UiEvent {
        data object FetchTopHeadlines : Event
        data class OnArticleClicked(val article: NewsArticle) : Event
        data class OnShareClicked(val article: NewsArticle) : Event
        data class OnBookmarkToggled(val article: NewsArticle) : Event
    }

    sealed interface Effect : UiEffect {

    }
}