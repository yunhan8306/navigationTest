package com.example.common.model

sealed interface NavigationType {
    object Swipe : NavigationType
    object Like : NavigationType
    object Message : NavigationType
    object MyPage : NavigationType
    object None : NavigationType
}