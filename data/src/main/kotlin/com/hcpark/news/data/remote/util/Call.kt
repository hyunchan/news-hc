package com.hcpark.news.data.remote.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

suspend fun <R> callCatching(call: suspend () -> R): Result<R> {
    @Suppress("TooGenericExceptionCaught")
    return try {
        Result.success(call())
    } catch (e: Exception) {
        Result.failure(e.wrap())
    }
}

fun <R> callToFlow(call: suspend () -> R): Flow<R> {
    return flow {
        emit(call())
    }.catch {
        throw it.wrap()
    }
}
