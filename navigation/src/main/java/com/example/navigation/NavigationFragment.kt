package com.example.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.common.base.BaseFragment
import com.example.common.extension.visible
import com.example.common.model.NavigationType
import com.example.navigation.databinding.FragmentNavigationBinding


class NavigationFragment : BaseFragment<FragmentNavigationBinding>() {

    private val viewModel by activityViewModels<NavigationViewModel>()

    override fun createFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNavigationBinding.inflate(inflater, container, false)

    override fun initFragment(savedInstanceState: Bundle?) {
        setDefaultNavigationMenu()
        addListeners()
        collectViewModel()
    }

    private fun setDefaultNavigationMenu() {
        viewModel.selectNavigation(NavigationType.SWIPE)
    }

    private fun addListeners() = with(binding) {
        clMainList.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.SWIPE)
        }

        clLikeHistory.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.LIKE)
        }

        clMessageList.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.MESSAGE)
        }

        clMyPage.setFirstClickEvent(200) {
            viewModel.selectNavigation(NavigationType.MYPAGE)
        }

    }

    private fun collectViewModel() = with(viewModel) {
        selectNavigationMenuState.launchInUiState(
            success = { type ->
                with(binding) {
                    // 뷰 반영
                    navigationBtnOffList.forEach { it.visible(true) }
                    navigationBtnOnList.forEach { it.visible(false) }

                    when(type) {
                        NavigationType.SWIPE -> {
                            imgSwipeOff.visible(false)
                            imgSwipeOn.visible(true)
                        }
                        NavigationType.LIKE -> {
                            imgLikeHistoryOff.visible(false)
                            imgLikeHistoryOn.visible(true)
                        }
                        NavigationType.MESSAGE -> {
                            imgMessageListOff.visible(false)
                            imgMessageListOn.visible(true)
                        }
                        NavigationType.MYPAGE -> {
                            imgMyPageOff.visible(false)
                            imgMyPageOn.visible(true)
                        }
                    }
                }
            }
        )
    }

    private val navigationBtnOnList by lazy {
        listOf(binding.imgSwipeOn, binding.imgLikeHistoryOn, binding.imgMessageListOn, binding.imgMyPageOn)
    }

    private val navigationBtnOffList by lazy {
        listOf(binding.imgSwipeOff, binding.imgLikeHistoryOff, binding.imgMessageListOff, binding.imgMyPageOff)
    }
}