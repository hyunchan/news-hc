package com.hcpark.news.presentation.news.contract

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object NewsContract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
        val category: Category = Category.Default
    ) : UiState

    sealed interface Event : UiEvent {
        data class OnArticleClick(val article: NewsArticle) : Event
        data class OnShareClick(val article: NewsArticle) : Event
        data class OnBookmarkClick(val article: NewsArticle) : Event
    }

    sealed interface Effect : UiEffect {

    }

    sealed class ModalState {
        data object Dismiss : ModalState()
    }
}