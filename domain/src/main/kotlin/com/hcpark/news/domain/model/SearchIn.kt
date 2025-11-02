package com.hcpark.news.domain.model

enum class SearchIn(val key: String) {
    Title("title"),
    Description("description"),
    Content("content");

    companion object {
        val Defaults = listOf(Title, Description)
    }
}
