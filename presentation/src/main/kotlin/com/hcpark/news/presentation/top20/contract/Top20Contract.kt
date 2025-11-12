package com.hcpark.news.presentation.top20.contract

import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object Top20Contract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
        val isLoading: Boolean = false,
        val fetchError: Throwable? = null,
        val articles: List<NewsArticle> = emptyList(),
        val bookmarkedUrls: Set<String> = emptySet()
    ) : UiState

    sealed class Event : UiEvent {
        data object OnRefresh : Event()
        data object OnDismissModal : Event()
        data object OnMoreNewsClick : Event()
        data class OnArticleClick(val model: NewsCardModel) : Event()
        data class OnSourceClick(val model: NewsCardModel) : Event()
        data class OnShareClick(val model: NewsCardModel) : Event()
        data class OnBookmarkClick(val model: NewsCardModel) : Event()
    }

    sealed class Effect : UiEffect {
        data class Launch(val route: String) : Effect()
        data class Toast(val message: String) : Effect()
    }

    sealed class ModalState {
        data object Dismiss : ModalState()
    }
}
