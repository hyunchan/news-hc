package com.hcpark.news.presentation.extension

import androidx.compose.foundation.lazy.LazyListState

fun LazyListState.isEmpty(): Boolean {
    return layoutInfo.visibleItemsInfo.isEmpty() || layoutInfo.totalItemsCount == 0
}

fun LazyListState.isLastItemVisible(): Boolean {
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty()) return false

    return visibleItemsInfo.last().index == layoutInfo.totalItemsCount - 1
}

fun LazyListState.isLastItemFullyVisible(): Boolean {
    if (!isLastItemVisible()) return false

    return layoutInfo.visibleItemsInfo.last().let { lastItem ->
        lastItem.offset + lastItem.size <= layoutInfo.viewportEndOffset
    }
}
