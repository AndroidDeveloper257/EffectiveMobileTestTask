package com.example.effectivemobiletesttask.vm

sealed class ApiStatus<T> {

    class Loading<T>: ApiStatus<T>()

    class Success<T: Any>(val response: T?): ApiStatus<T>()

    class Error<T: Any>(val error: Throwable): ApiStatus<T>()

}