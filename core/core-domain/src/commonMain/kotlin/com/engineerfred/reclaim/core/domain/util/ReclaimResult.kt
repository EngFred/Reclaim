package com.engineerfred.reclaim.core.domain.util

sealed class ReclaimResult<out T> {
    data class Success<T>(val data: T) : ReclaimResult<T>()
    data class Failure(val exception: Throwable) : ReclaimResult<Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isFailure: Boolean get() = this is Failure

    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Failure -> null
    }

    fun exceptionOrNull(): Throwable? = when (this) {
        is Success -> null
        is Failure -> exception
    }

    fun <R> map(transform: (T) -> R): ReclaimResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Failure -> this
    }

    fun onSuccess(action: (T) -> Unit): ReclaimResult<T> {
        if (this is Success) action(data)
        return this
    }

    fun onFailure(action: (Throwable) -> Unit): ReclaimResult<T> {
        if (this is Failure) action(exception)
        return this
    }
}

inline fun <T> reclaimRunCatching(block: () -> T): ReclaimResult<T> = try {
    ReclaimResult.Success(block())
} catch (e: Exception) {
    ReclaimResult.Failure(e)
}