package com.hcpark.news.presentation.top20.viewmodel

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.usecase.BookmarkUseCase
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlesUseCase
import com.hcpark.news.domain.usecase.ObserveBookmarkedUrlsUseCase
import com.hcpark.news.domain.usecase.UnbookmarkUseCase
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.component.asResource
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
    private val observeBookmarkedUrlsUseCase: ObserveBookmarkedUrlsUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val unbookmarkUseCase: UnbookmarkUseCase,
) : MVIViewModel<Event, State, Effect>() {

    init {
        observes()
        fetch()
    }

    private fun observes() = viewModelScope.launch {
        observeBookmarkedUrlsUseCase().asResource().collect { resource ->
            resource.success {
                setState { copy(bookmarkedUrls = it) }
            }.failure {
                setState { copy(bookmarkedUrls = emptySet()) }
            }
        }
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnRefresh -> fetch()
            is Event.OnDismissModal -> dismissModal()
            Event.OnMoreNewsClick -> launchNews()
            is Event.OnArticleClick -> openLink(event.model)
            is Event.OnSourceClick -> launchNews(event.model)
            is Event.OnBookmarkClick -> toggleBookmark(event.model)
            is Event.OnShareClick -> share(event.model)
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

    private fun launchNews(model: NewsCardModel) {
        setEffect(Effect.Launch(MainRoute.news(sources = model.sourceId)))
    }

    private fun openLink(model: NewsCardModel) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(model.url)
        }
        setEffect(Effect.LaunchIntent(intent))
    }

    private fun toggleBookmark(model: NewsCardModel) = viewModelScope.launch {
        runCatching {
            if (model.isBookmarked) {
                unbookmarkUseCase(model.url).getOrThrow()
                "북마크가 해제되었습니다"
            } else {
                bookmarkUseCase(
                    url = model.url,
                    title = model.title,
                    description = model.description,
                    imageUrl = model.imageUrl,
                    sourceId = model.sourceId,
                    sourceName = model.sourceName,
                    publishedAt = model.publishedAt
                ).getOrThrow()
                "북마크가 추가되었습니다"
            }
        }.onSuccess {
            setEffect(Effect.Toast(it))
        }.onFailure {
            setEffect(Effect.Toast("북마크 오류 : ${it.message}"))
        }
    }

    private fun share(model: NewsCardModel) {
        // todo
    }
}
