package com.example.common.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.collect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseViewModel(
    private val app: Application
) : AndroidViewModel(app) {

    protected val appContext: Context get() = app.applicationContext

    protected fun <T> Flow<T>.onResult(action: (T) -> Unit) {
        flowOn(Dispatchers.IO).collect(viewModelScope, action)
    }

}