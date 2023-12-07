package com.example.common

import com.example.common.state.ResultState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart


/** Flow 내 데이터를 ResultState로 묶어 매핑 및 emit */
fun <T> Flow<T>.resultState(): Flow<ResultState<T>> {
    return this
        .map<T, ResultState<T>> {
            ResultState.Success(it)
        }
        .onStart { emit(ResultState.Loading) }
        .catch { emit(ResultState.Error(it)) }
        .onCompletion { emit(ResultState.Finish) }
}

/** 로딩, 성공, 에러, 에러 with data, 완료시 각각 다른 처리를 해야할 때 사용 */
fun <T> Flow<ResultState<T>>.onUiState(
    loading: suspend () -> Unit = {},
    success: suspend (T) -> Unit = {},
    error: suspend (Throwable?) -> Unit = {},
    errorWithData: suspend (Pair<Throwable?, T>) -> Unit = {},
    finish: suspend () -> Unit = {}
): Flow<ResultState<T>> = onEach { state ->
    when (state) {
        ResultState.Loading -> {
            loading.invoke()
        }
        is ResultState.Success -> {
            success.invoke(state.data)
        }
        is ResultState.Error -> {
            error.invoke(state.error)
        }
        is ResultState.ErrorWithData -> {
            errorWithData.invoke(Pair(state.error, state.data))
        }
        is ResultState.Finish -> {
            finish.invoke()
        }
        else -> Unit
    }
}

/** 로딩, 성공, 에러, 에러 with data, 완료 모두 동일한 처리를 할 때 사용
 * 예) 반환 데이터를 하나의 StateFlow에 대입해서 처리할 경우 */
inline fun <T> Flow<ResultState<T>>.onState(
    coroutineScope: CoroutineScope,
    crossinline collect: (ResultState<T>) -> Unit
) {
    onUiState(
        loading = { collect(ResultState.Loading) },
        success = { collect(ResultState.Success(it)) },
        error = { collect(ResultState.Error(it)) },
        errorWithData = { collect(ResultState.ErrorWithData(it.first, it.second)) },
        finish = { collect(ResultState.Finish) }
    ).launchIn(coroutineScope)
}

inline fun <T, F> Flow<ResultState<T>>.onStateAppend(
    coroutineScope: CoroutineScope,
    arguments: F,
    crossinline collect: (ResultState<Pair<T, F>>) -> Unit
) {
    onUiState(
        loading = { collect(ResultState.Loading) },
        success = { collect(ResultState.Success(Pair(it, arguments))) },
        error = { collect(ResultState.Error(it)) },
        finish = { collect(ResultState.Finish) }
    ).launchIn(coroutineScope)
}

fun <T> Flow<T>.onError(callback: suspend (error: Throwable) -> Unit) = catch { error ->
    error.printStackTrace()
    callback(error)
}

inline fun <T> Flow<T>.collect(
    externalScope: CoroutineScope,
    crossinline collect: (T) -> Unit
) = onEach { collect.invoke(it) }.launchIn(externalScope)

// 마지막 발행 시간과 현재 시간 비교해서 이벤트 발행, 나머지는 무시.
fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEmissionTime > windowDuration) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}