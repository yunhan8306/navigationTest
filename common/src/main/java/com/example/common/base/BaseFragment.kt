package com.example.common.base

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.example.common.extension.LifecycleOwnerWrapper

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment(), LifecycleOwnerWrapper {

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    protected abstract fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Binding

    protected open fun initFragment(savedInstanceState: Bundle?) = Unit

    protected open fun initBinding() = Unit

    override fun initLifecycleOwner(): LifecycleOwner = viewLifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = createFragmentBinding(inflater, container).apply {
        lifecycleOwner = viewLifecycleOwner
        _binding = this
        initBinding()
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment(savedInstanceState)
        Log.d("YHYH", "${this.javaClass} - onViewCreated called")
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun isFragmentDestroying() = _binding == null

    val Number.dp: Float get() = this.toDp(requireContext())
    val Number.px: Int get() = this.toPx(requireContext())

    fun Number.toPx(context: Context): Int {
        val densityDpi = context.resources.displayMetrics.densityDpi
        return (this.toFloat() * (densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    fun Number.toDp(context: Context): Float {
        val densityDpi = context.resources.displayMetrics.densityDpi
        return this.toFloat() / (densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}