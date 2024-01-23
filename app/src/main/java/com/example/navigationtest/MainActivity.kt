package com.example.navigationtest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseActivity
import com.example.common.extension.LifecycleOwnerWrapper
import com.example.like.LikeFragment
import com.example.message.MessageListFragment
import com.example.mypage.MyPageFragment
import com.example.navigation.NavigationFragment
import com.example.navigationtest.extension.addFragment
import com.example.common.model.NavigationType
import com.example.common.onUiState
import com.example.navigation.NavigationViewModel
import com.example.navigationtest.databinding.ActivityMainBinding
import com.example.navigationtest.extension.hideFragment
import com.example.navigationtest.extension.showFragment
import com.example.swipe.SwipeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), LifecycleOwnerWrapper {

    private val navigationViewModel by viewModels<NavigationViewModel>()

    private var swipeFragment: SwipeFragment? = null
    private var likeFragment: LikeFragment? = null
    private var messageListFragment: MessageListFragment? = null
    private var myPageFragment: MyPageFragment? = null
    private var navigationFragment: NavigationFragment? = null

    private var onTopFragment : Fragment? = null
    private var startNavigationType: NavigationType = NavigationType.Swipe



    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initActivity(savedInstanceState: Bundle?) {
        collectViewModel()
        addNavigationFragment()
        navigationViewModel.selectNavigation(startNavigationType)
    }

    private fun collectViewModel() = with(navigationViewModel) {
        navigationState.onResult { navigationType ->
            selectOnTopFragment(navigationType)
        }
    }
    private fun selectOnTopFragment(navigationType: NavigationType) {
        hideFragment(onTopFragment)

        when(navigationType) {
            is NavigationType.Swipe -> {
                if(swipeFragment == null) {
                    swipeFragment = SwipeFragment()
                    addFragment(R.id.containerMain, swipeFragment)
                } else {
                    showFragment(swipeFragment)
                }
            }
            is NavigationType.Like -> {
                if(likeFragment == null) {
                    likeFragment = LikeFragment()
                    addFragment(R.id.containerMain, likeFragment)
                } else {
                    showFragment(likeFragment)
                }
            }
            is NavigationType.Message -> {
                if(messageListFragment == null) {
                    messageListFragment = MessageListFragment()
                    addFragment(R.id.containerMain, messageListFragment)
                } else {
                    showFragment(messageListFragment)
                }
            }
            is NavigationType.MyPage -> {
                if(myPageFragment == null) {
                    myPageFragment = MyPageFragment()
                    addFragment(R.id.containerMain, myPageFragment)
                } else {
                    showFragment(myPageFragment)
                }
            }
            is NavigationType.None -> Unit
        }

        onTopFragment = getNavigationFragment(navigationType)
    }


    private fun getNavigationFragment(navigationType: NavigationType) =
        when(navigationType) {
            is NavigationType.Swipe -> swipeFragment
            is NavigationType.Like -> likeFragment
            is NavigationType.Message -> messageListFragment
            is NavigationType.MyPage -> myPageFragment
            is NavigationType.None -> null
        }


    private fun addNavigationFragment() {
        navigationFragment = NavigationFragment()
        addFragment(R.id.containerNavigation, navigationFragment)
    }
}