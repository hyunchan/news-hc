package com.hcpark.news.domain.usecase

import com.hcpark.news.domain.model.NewsArticle
import javax.inject.Inject

class ToggleBookmarkNewsArticleUseCase @Inject constructor(
    private val getIsBookmarkedUseCase: GetIsBookmarkedUseCase,
    private val bookmarkNewsArticleUseCase: BookmarkNewsArticleUseCase,
    private val unbookmarkNewsArticleUseCase: UnbookmarkNewsArticleUseCase
) {
    suspend operator fun invoke(newsArticle: NewsArticle): Result<Boolean> =
        getIsBookmarkedUseCase(newsArticle.url).mapCatching { bookmarked ->
            if (bookmarked) {
                unbookmarkNewsArticleUseCase(newsArticle).getOrThrow()
                false
            } else {
                bookmarkNewsArticleUseCase(newsArticle).getOrThrow()
                true
            }
        }
}
