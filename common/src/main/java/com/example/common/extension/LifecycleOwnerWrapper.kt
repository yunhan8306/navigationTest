package com.example.common.extension

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.common.collect
import com.example.common.throttleFirst
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface LifecycleOwnerWrapper {

    fun initLifecycleOwner(): LifecycleOwner

    fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) =
        liveData.observe(this, Observer(body))

    fun <T> Flow<T>.onResult(action: (T) -> Unit) {
        collect(initLifecycleOwner().lifecycleScope, action)
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
}