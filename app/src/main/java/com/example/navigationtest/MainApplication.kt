package com.example.navigationtest

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
    }
}