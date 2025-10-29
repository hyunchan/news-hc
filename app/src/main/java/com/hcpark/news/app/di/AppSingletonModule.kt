package com.hcpark.news.app.di

import com.hcpark.news.app.BuildConfig
import com.hcpark.news.domain.ApiConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppSingletonModule {

    @Provides
    @Singleton
    fun provideApiConfig(): ApiConfig {
        return object : ApiConfig {
            override val key: String = BuildConfig.API_KEY
            override val host: String = BuildConfig.API_HOST
        }
    }
}
