package com.hcpark.news.presentation.news.contract

import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.NewsSource
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object NewsContract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
        val category: Category = Category.Default,
        val sources: String? = null
    ) : UiState

    sealed interface Event : UiEvent {
        data class OnArticleClick(val article: NewsArticle) : Event
        data class OnSourceClick(val source: NewsSource) : Event
        data class OnShareClick(val article: NewsArticle) : Event
        data class OnBookmarkClick(val article: NewsArticle) : Event
        data class OnCategoryChange(val category: Category) : Event
    }

    sealed interface Effect : UiEffect {
        data class Launch(val route: String) : Effect
        data class Toast(val message: String) : Effect
    }

    sealed class ModalState {
        data object Dismiss : ModalState()
    }
}