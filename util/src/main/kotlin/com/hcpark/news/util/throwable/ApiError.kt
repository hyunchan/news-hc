package com.hcpark.news.util.throwable

data class ApiError(
    val httpStatusCode: Int,
    val code: String,
    override val message: String
) : Throwable(message)
