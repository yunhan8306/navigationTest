package com.example.common.extension

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.common.collect
import com.example.common.onUiState
import com.example.common.state.ResultState
import com.example.common.throttleFirst
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

interface LifecycleOwnerWrapper {

    fun initLifecycleOwner(): LifecycleOwner

    fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) =
        liveData.observe(this, Observer(body))

    fun <T> Flow<ResultState<T>>.launchInUiState(
        loading: suspend () -> Unit = {},
        success: suspend (T) -> Unit = {},
        error: suspend (Throwable?) -> Unit = {},
        errorWithData: suspend (Pair<Throwable?, T>) -> Unit = {},
        finish: suspend () -> Unit = {},
    ): Job = onUiState(
        loading,
        success,
        error,
        errorWithData,
        finish
    ).launchIn(initLifecycleOwner().lifecycleScope)

    fun <T> Flow<T>.onResult(action: (T) -> Unit) {
        collect(initLifecycleOwner().lifecycleScope, action)
    }

    fun <T> LiveData<T>.observe(observer: Observer<T>) {
        observe(initLifecycleOwner(), observer)
    }

    fun View.setFirstClickEvent(
        windowDuration: Long = 1000,
        onClick: () -> Unit,
    ) {
        clicks()
            .throttleFirst(windowDuration)
            .onEach { onClick.invoke() }
            .launchIn(initLifecycleOwner().lifecycleScope)
    }

    fun View.setBlockClickEvent(
        windowDuration: Long = 1000,
        onClick: () -> Unit,
    ) {
        clicks()
            .onEach {
                if(isClickable){
                    this.isClickable = false
                    onClick.invoke()

                    initLifecycleOwner().lifecycleScope.launch{
                        delay(windowDuration)
                        this@setBlockClickEvent.isClickable = true
                    }
                }
            }
            .launchIn(initLifecycleOwner().lifecycleScope)
    }

}