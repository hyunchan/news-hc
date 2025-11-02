package com.hcpark.news.data.remote.util

import io.ktor.client.plugins.ResponseException
import kotlinx.serialization.SerializationException
import okio.IOException

suspend fun Throwable.wrap(): Throwable {
    return when (this) {
        is ResponseException -> response.toApiError()
        is IOException -> Exception("Network error", this)
        is SerializationException -> Exception("Parsing error", this)
        else -> this
    }
}
