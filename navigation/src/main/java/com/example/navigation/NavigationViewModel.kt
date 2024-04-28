package com.example.navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.model.NavigationSideEffect
import com.example.common.model.NavigationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    app: Application,
): AndroidViewModel(app) {

    private val _navigationState = MutableStateFlow<NavigationType>(NavigationType.None)
    val navigationState = _navigationState.asStateFlow()

    private val _navigationSideEffect = MutableSharedFlow<NavigationSideEffect>()
    val navigationSideEffect = _navigationSideEffect.asSharedFlow()

    fun selectNavigation(menu: NavigationType) {
        viewModelScope.launch {
            if(menu == _navigationState.value) {
                _navigationSideEffect.emit(NavigationSideEffect.ScrollTop)
            } else {
                _navigationState.value = menu
            }
        }
    }
}