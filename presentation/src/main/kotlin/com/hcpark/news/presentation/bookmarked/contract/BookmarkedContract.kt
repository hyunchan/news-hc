package com.hcpark.news.presentation.bookmarked.contract

import android.content.Intent
import com.hcpark.news.domain.model.BookmarkedArticle
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object BookmarkedContract {
    data class State(
        val articles: List<BookmarkedArticle> = emptyList(),
        val isLoading: Boolean = false,
        val fetchError: Throwable? = null,
        val modalState: ModalState = ModalState.None,
    ) : UiState

    sealed class Event : UiEvent {
        data object OnModalDismiss : Event()
        data class OnArticleClick(val model: NewsCardModel) : Event()
        data class OnBookmarkClick(val model: NewsCardModel) : Event()
        data class OnShareClick(val model: NewsCardModel) : Event()
    }

    sealed class Effect : UiEffect {
        data class Launch(val route: String) : Effect()
        data class Toast(val message: String) : Effect()
        data class LaunchIntent(val intent: Intent) : Effect()
    }

    sealed class ModalState {
        data object None : ModalState()
    }
}
