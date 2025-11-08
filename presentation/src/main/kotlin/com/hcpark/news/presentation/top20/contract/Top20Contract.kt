package com.hcpark.news.presentation.top20.contract

import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object Top20Contract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
        val articles: List<NewsArticle> = emptyList()
    ) : UiState

    sealed class Event : UiEvent {
        data object OnRefresh : Event()
        data object OnDismissModal : Event()
        data class OnArticleClick(val article: NewsArticle) : Event()
        data class OnShareClick(val article: NewsArticle) : Event()
        data class OnBookmarkClick(val article: NewsArticle) : Event()
    }

    sealed class Effect : UiEffect {
        data class Launch(val route: String) : Effect()
    }

    sealed class ModalState {
        data object Dismiss : ModalState()
    }
}
