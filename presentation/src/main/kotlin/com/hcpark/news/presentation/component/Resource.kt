package com.hcpark.news.presentation.component

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Resource<Entity> {

    suspend fun loading(listener: OnLoadListener): Resource<Entity> {
        listener(this is Loading)
        return this
    }

    suspend fun success(listener: OnSuccessListener<Entity>): Resource<Entity> {
        if (this is Success) listener(data)
        return this
    }

    suspend fun failure(listener: OnFailureListener): Resource<Entity> {
        if (this is Failure) listener(throwable)
        return this
    }

    class Loading<Entity> : Resource<Entity>()
    class Success<Entity>(val data: Entity) : Resource<Entity>()
    class Failure<Entity>(val throwable: Throwable) : Resource<Entity>()

    fun interface OnLoadListener {
        suspend operator fun invoke(onLoad: Boolean)
    }

    fun interface OnSuccessListener<Entity> {
        suspend operator fun invoke(value: Entity)
    }

    fun interface OnFailureListener {
        suspend operator fun invoke(throwable: Throwable)
    }

    companion object {
        operator fun <T> invoke(data: T): Resource<T> = Success(data)
        operator fun <T> invoke(throwable: Throwable): Resource<T> = Failure(throwable)
    }
}

fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
    return map {
        Resource(it)
    }.catch { error ->
        emit(Resource(error))
    }.onStart {
        emit(Resource.Loading())
    }
}
