package com.example.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.common.base.BaseFragment
import com.example.common.extension.visible
import com.example.common.model.NavigationSideEffect
import com.example.common.model.NavigationType
import com.example.navigation.databinding.FragmentNavigationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class NavigationFragment @Inject constructor() : BaseFragment<FragmentNavigationBinding>() {

    private val viewModel by activityViewModels<NavigationViewModel>()

    private val navigationBtnOnList by lazy {
        listOf(binding.imgSwipeOn, binding.imgLikeHistoryOn, binding.imgMessageListOn, binding.imgMyPageOn)
    }

    private val navigationBtnOffList by lazy {
        listOf(binding.imgSwipeOff, binding.imgLikeHistoryOff, binding.imgMessageListOff, binding.imgMyPageOff)
    }

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNavigationBinding.inflate(inflater, container, false)

    override fun initFragment(savedInstanceState: Bundle?) {
        addListeners()
        collectViewModel()
    }
    private fun addListeners() = with(binding) {
        clMainList.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.Swipe)
        }

        clLikeHistory.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.Like)
        }

        clMessageList.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.Message)
        }

        clMyPage.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.MyPage)
        }
    }

    private fun collectViewModel() = with(viewModel) {
        navigationState.onResult { setNavigationBtn(it) }
    }

    private fun setNavigationBtn(navigationType: NavigationType) = with(binding) {
        navigationBtnOffList.forEach { it.visible(true) }
        navigationBtnOnList.forEach { it.visible(false) }

        when(navigationType) {
            is NavigationType.Swipe -> {
                imgSwipeOff.visible(false)
                imgSwipeOn.visible(true)
            }
            is NavigationType.Like -> {
                imgLikeHistoryOff.visible(false)
                imgLikeHistoryOn.visible(true)
            }
            is NavigationType.Message -> {
                imgMessageListOff.visible(false)
                imgMessageListOn.visible(true)
            }
            is NavigationType.MyPage -> {
                imgMyPageOff.visible(false)
                imgMyPageOn.visible(true)
            }
            is NavigationType.None -> Unit
        }
    }
}