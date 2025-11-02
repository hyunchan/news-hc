package com.hcpark.news.domain.model

/**
 * 무료 버전에서 기본값만 지원
 */
enum class Country(val key: String) {
    US("us");

    companion object {
        val Default = US
    }
}
