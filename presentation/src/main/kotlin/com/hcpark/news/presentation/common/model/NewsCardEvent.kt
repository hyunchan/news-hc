package com.hcpark.news.presentation.common.model

sealed class NewsCardEvent {
    data object CardClick : NewsCardEvent()
    data object SourceClick : NewsCardEvent()
    data object ShareClick : NewsCardEvent()
    data object BookmarkClick : NewsCardEvent()
}
