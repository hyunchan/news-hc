package com.hcpark.news.presentation.top20.viewmodel

import androidx.lifecycle.viewModelScope
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.model.NewsArticle
import com.hcpark.news.domain.model.NewsSource
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlesUseCase
import com.hcpark.news.domain.usecase.ToggleBookmarkNewsArticleUseCase
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.main.navigation.MainRoute
import com.hcpark.news.presentation.top20.contract.Top20Contract.Effect
import com.hcpark.news.presentation.top20.contract.Top20Contract.Event
import com.hcpark.news.presentation.top20.contract.Top20Contract.ModalState
import com.hcpark.news.presentation.top20.contract.Top20Contract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Top20ViewModel @Inject constructor(
    private val getTopHeadlineArticlesUseCase: GetTopHeadlineArticlesUseCase,
    private val toggleBookmarkNewsArticleUseCase: ToggleBookmarkNewsArticleUseCase
) : MVIViewModel<Event, State, Effect>() {

    init {
        fetch()
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnRefresh -> fetch()
            is Event.OnDismissModal -> dismissModal()
            Event.OnMoreNewsClick -> launchNews()
            is Event.OnArticleClick -> openLink(event.article)
            is Event.OnSourceClick -> launchNews(event.source)
            is Event.OnBookmarkClick -> toggleBookmark(event.article)
            is Event.OnShareClick -> share(event.article)
        }
    }

    private fun setModalState(modalState: ModalState) {
        setState { copy(modalState = modalState) }
    }

    private fun fetch() = viewModelScope.launch {
        setState { copy(isLoading = true, fetchError = null) }
        getTopHeadlineArticlesUseCase(
            country = Country.US,
            size = 20
        ).onSuccess {
            setState { copy(articles = it) }
        }.onFailure {
            setState { copy(fetchError = it) }
        }
        setState { copy(isLoading = false) }
    }

    private fun dismissModal() {
        setModalState(ModalState.Dismiss)
    }

    private fun launchNews() {
        setEffect(Effect.Launch(MainRoute.news()))
    }

    private fun launchNews(source: NewsSource) {
        setEffect(Effect.Launch(MainRoute.news(sources = source.id)))
    }

    private fun openLink(article: NewsArticle) {
        // todo
    }

    private fun toggleBookmark(article: NewsArticle) = viewModelScope.launch {
        toggleBookmarkNewsArticleUseCase(article).map {
            if (it) "북마크가 추가되었습니다"
            else "북마크가 해제되었습니다"
        }.onSuccess {
            setEffect(Effect.Toast(it))
        }.onFailure {
            setEffect(Effect.Toast("북마크 오류 : ${it.message}"))
        }
    }

    private fun share(article: NewsArticle) {
        // todo
    }
}
