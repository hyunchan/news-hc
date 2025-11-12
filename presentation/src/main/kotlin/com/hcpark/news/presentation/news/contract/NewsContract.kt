package com.hcpark.news.presentation.news.contract

import android.content.Intent
import com.hcpark.news.domain.model.Category
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.UiEffect
import com.hcpark.news.presentation.component.UiEvent
import com.hcpark.news.presentation.component.UiState

object NewsContract {
    data class State(
        val modalState: ModalState = ModalState.Dismiss,
        val category: Category = Category.Default,
        val sources: String? = null
    ) : UiState {
        val filterVisible = sources == null
    }

    sealed interface Event : UiEvent {
        data class OnArticleClick(val model: NewsCardModel) : Event
        data class OnSourceClick(val model: NewsCardModel) : Event
        data class OnShareClick(val model: NewsCardModel) : Event
        data class OnBookmarkClick(val model: NewsCardModel) : Event
        data class OnCategoryChange(val category: Category) : Event
    }

    sealed interface Effect : UiEffect {
        data class Launch(val route: String) : Effect
        data class Toast(val message: String) : Effect
        data class LaunchIntent(val intent: Intent) : Effect
    }

    sealed class ModalState {
        data object Dismiss : ModalState()
    }
}