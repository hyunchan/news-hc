package com.hcpark.news.domain.model

/**
 * 기사 검색 결과를 정렬하는 방법을 나타냅니다.
 *
 * - Relevancy: 관련성이 높은 기사가 먼저 표시됩니다.
 * - Popularity: 인기 있는 출처와 출판사의 기사가 먼저 표시됩니다.
 * - PublishedAt: 최신 기사가 먼저 표시됩니다.
 */
@Suppress("SpellCheckingInspection", "unused")
enum class SortBy(val key: String) {
    Relevancy("relevancy"),
    Popularity("popularity"),
    PublishedAt("publishedAt");

    companion object {
        val Default = PublishedAt
    }
}
