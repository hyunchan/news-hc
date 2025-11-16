package com.hcpark.news.presentation.bookmarked.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.hcpark.news.domain.usecase.ObserveBookmarkedArticleUseCase
import com.hcpark.news.domain.usecase.UnbookmarkUseCase
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Effect
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.Event
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.ModalState
import com.hcpark.news.presentation.bookmarked.contract.BookmarkedContract.State
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkedViewModel @Inject constructor(
    observeBookmarkedArticleUseCase: ObserveBookmarkedArticleUseCase,
    private val unbookmarkUseCase: UnbookmarkUseCase,
) : MVIViewModel<Event, State, Effect>() {

    val newsCardModelPagingData = observeBookmarkedArticleUseCase()
        .map { pagingData ->
            pagingData.map { NewsCardModel(it) }
        }
        .cachedIn(viewModelScope)

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnModalDismiss -> dismissModal()
            is Event.OnArticleClick -> openLink(event.model)
            is Event.OnBookmarkClick -> unbookmark(event.model)
            is Event.OnShareClick -> share(event.model)
        }
    }

    private fun setModalState(modalState: ModalState) {
        setState { copy(modalState = modalState) }
    }

    private fun dismissModal() {
        setModalState(ModalState.None)
    }

    private fun openLink(model: NewsCardModel) {
        setEffect(Effect.LaunchIntent(model.viewIntent()))
    }

    private fun unbookmark(model: NewsCardModel) = viewModelScope.launch {
        runCatching {
            unbookmarkUseCase(model.url).getOrThrow()
            "북마크가 해제되었습니다"
        }.onSuccess {
            setEffect(Effect.Toast(it))
        }.onFailure {
            setEffect(Effect.Toast("북마크 오류 : ${it.message}"))
        }
    }

    private fun share(model: NewsCardModel) {
        setEffect(Effect.LaunchIntent(model.shareIntent()))
    }
}
