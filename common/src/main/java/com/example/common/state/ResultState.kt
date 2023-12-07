package com.example.common.state

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> mutableResultState(
    uiState: ResultState<T> = ResultState.UnInitialize
): MutableStateFlow<ResultState<T>> = MutableStateFlow(uiState)

sealed interface ResultState<out T> {
    object UnInitialize : ResultState<Nothing>
    data class Success<T>(val data: T) : ResultState<T>
    data class Error(val error: Throwable? = null) : ResultState<Nothing>
    data class ErrorWithData<T>(val error: Throwable? = null, val data: T) : ResultState<T>
    object Loading : ResultState<Nothing>
    object Finish : ResultState<Nothing>
}

fun <T> ResultState<T>.onUiState(
    loading: () -> Unit = {},
    success: (T) -> Unit = {},
    error: (Throwable?) -> Unit = {},
    errorWithData: (Pair<Throwable?, T>) -> Unit = {},
    finish: () -> Unit = {}
) = when (this) {
    ResultState.Loading -> loading.invoke()
    is ResultState.Success -> success.invoke(this.data)
    is ResultState.Error -> error.invoke(this.error)
    is ResultState.ErrorWithData -> errorWithData.invoke(Pair(this.error, this.data))
    is ResultState.Finish -> finish.invoke()
    else -> {}
}