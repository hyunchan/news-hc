package com.hcpark.news.presentation.news.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.hcpark.news.domain.model.Category
import com.hcpark.news.domain.model.Country
import com.hcpark.news.domain.usecase.BookmarkUseCase
import com.hcpark.news.domain.usecase.GetTopHeadlineArticlePagingSourceUseCase
import com.hcpark.news.domain.usecase.ObserveBookmarkedUrlsUseCase
import com.hcpark.news.domain.usecase.UnbookmarkUseCase
import com.hcpark.news.presentation.common.model.NewsCardModel
import com.hcpark.news.presentation.component.MVIViewModel
import com.hcpark.news.presentation.main.navigation.MainRoute
import com.hcpark.news.presentation.news.contract.NewsContract.Effect
import com.hcpark.news.presentation.news.contract.NewsContract.Event
import com.hcpark.news.presentation.news.contract.NewsContract.ModalState
import com.hcpark.news.presentation.news.contract.NewsContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTopHeadlineArticlePagingSourceUseCase: GetTopHeadlineArticlePagingSourceUseCase,
    private val observeBookmarkedUrlsUseCase: ObserveBookmarkedUrlsUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
    private val unbookmarkUseCase: UnbookmarkUseCase,
) : MVIViewModel<Event, State, Effect>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingModel = uiState
        .map { it.category to it.sources }
        .distinctUntilChanged()
        .flatMapLatest { (category, sources) ->
            Pager(
                config = PagingConfig(pageSize = 20),
                pagingSourceFactory = {
                    getTopHeadlineArticlePagingSourceUseCase(
                        country = if (sources == null) Country.US else null,
                        category = category,
                        sources = sources
                    )
                }
            ).flow.cachedIn(viewModelScope)
                .combine(observeBookmarkedUrlsUseCase()) { pagingData, bookmarkedUrls ->
                    pagingData.map { article ->
                        NewsCardModel(article, bookmarkedUrls.contains(article.url))
                    }
                }
        }

    init {
        init(savedStateHandle)
    }

    private fun init(savedStateHandle: SavedStateHandle) {
        val category = savedStateHandle.get<String>(MainRoute.CATEGORY)
            .let(Category::fromKey)
        val sources = savedStateHandle.get<String>(MainRoute.SOURCES)
            ?.takeIf { it != "null" }
        setState { copy(category = category, sources = sources) }
    }

    override fun createInitialState(): State {
        return State()
    }

    override fun handleEvent(event: Event) {
        when (event) {
            is Event.OnArticleClick -> openLink(event.model)
            is Event.OnSourceClick -> launchNews(event.model)
            is Event.OnBookmarkClick -> toggleBookmark(event.model)
            is Event.OnShareClick -> share(event.model)
            is Event.OnCategoryChange -> updateCategory(event.category)
            Event.OnModalDismiss -> dismissModal()
            Event.OnReturnToMainClick -> showConfirmReturnToMain()
            Event.OnReturnToMainConfirm -> confirmReturnToMain()
        }
    }

    private fun setModal(modalState: ModalState) {
        setState { copy(modalState = modalState) }
    }

    private fun openLink(model: NewsCardModel) {
        setEffect(Effect.LaunchIntent(model.viewIntent()))
    }

    private fun launchNews(model: NewsCardModel) {
        if (model.sourceId != null) {
            setEffect(Effect.Launch(MainRoute.news(sources = model.sourceId)))
        }
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
        setEffect(Effect.LaunchIntent(model.shareIntent()))
    }

    private fun updateCategory(category: Category) {
        setState { copy(category = category) }
    }

    private fun dismissModal() {
        setModal(ModalState.None)
    }

    private fun showConfirmReturnToMain() {
        setModal(ModalState.ConfirmReturnToMain)
    }

    private fun confirmReturnToMain() {
        setModal(ModalState.None)
        setEffect(Effect.NavigateToMain)
    }
}
