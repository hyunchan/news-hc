package com.hcpark.news.data.remote.util

import com.hcpark.news.data.remote.dto.ErrorResponse
import com.hcpark.news.util.throwable.ApiError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend fun HttpResponse.toApiError(): ApiError {
    val httpStatusCode = status.value
    val errorResponse: ErrorResponse = body()
    return ApiError(httpStatusCode, errorResponse.code, errorResponse.message)
}
