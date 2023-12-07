package com.example.common.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.example.common.extension.LifecycleOwnerWrapper

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(), LifecycleOwnerWrapper {

    protected val binding: Binding by lazy { createBinding() }

    protected abstract fun createBinding(): Binding

    protected open fun initActivity(savedInstanceState: Bundle?) = Unit

    override fun initLifecycleOwner(): LifecycleOwner = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        initActivity(savedInstanceState)
    }




}