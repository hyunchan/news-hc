package com.hcpark.news.domain.model

/**
 * 뉴스 검색에 사용할 카테고리를 나타냅니다.
 */
@Suppress("unused")
enum class Category(val key: String?) {
    All(null),
    Business("business"),
    Entertainment("entertainment"),
    General("general"),
    Health("health"),
    Science("science"),
    Sports("sports"),
    Technology("technology");

    companion object {
        val Default: Category = All
    }
}
