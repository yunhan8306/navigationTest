package com.example.navigation

import android.app.Application
import android.util.Log
import com.example.common.base.BaseViewModel
import com.example.common.model.NavigationType
import com.example.common.state.ResultState
import com.example.common.state.mutableResultState
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class NavigationViewModel @Inject constructor(
    app: Application,
): BaseViewModel(app) {
    private val _selectNavigationMenuState = mutableResultState<NavigationType>()
    val selectNavigationMenuState = _selectNavigationMenuState.asStateFlow()

    private val _scrollTopState = mutableResultState<Pair<NavigationType, Boolean>>()
    val scrollTopState = _scrollTopState.asStateFlow()


    fun selectNavigation(menu: NavigationType) {

        if(_selectNavigationMenuState.value == ResultState.Success(menu)) {
            // 기존 선택한 메뉴와 같을 때
            Log.d("YHYH", "동일 - $menu")
            _scrollTopState.value = ResultState.Success(Pair(menu, true))
        } else {
            Log.d("YHYH", "변경 - $menu")
            _selectNavigationMenuState.value = ResultState.Success(menu)
        }
    }

    fun resetScrollTopState(menu: NavigationType) {
        _scrollTopState.value = ResultState.Success(Pair(menu, false))
    }
}