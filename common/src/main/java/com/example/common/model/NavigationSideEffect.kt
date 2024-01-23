package com.example.common.model

sealed interface NavigationSideEffect {
    object ScrollTop : NavigationSideEffect
    object Destroy : NavigationSideEffect
}