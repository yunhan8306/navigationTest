package com.example.navigationtest

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
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
import com.example.navigationtest.extension.replaceFragment
import com.example.navigationtest.extension.showFragment
import com.example.swipe.SwipeFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn


//@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), LifecycleOwnerWrapper {

    private var swipeFragment: SwipeFragment? = null
    private var likeFragment: LikeFragment? = null
    private var messageListFragment: MessageListFragment? = null
    private var myPageFragment: MyPageFragment? = null
    private var navigationFragment: NavigationFragment? = null

    private var onTopFragment : Fragment? = null

    private val navigationViewModel by viewModels<NavigationViewModel>()


    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initActivity(savedInstanceState: Bundle?) {
        collectViewModel()
        addNavigationFragment()
        setDefaultNavigation()
    }

    private fun collectViewModel() {
        with(navigationViewModel) {
            selectNavigationMenuState.onUiState(
                success = {
                    selectMainViewFragment(it)
                }
            ).launchIn(initLifecycleOwner().lifecycleScope)

            scrollTopState.onUiState(
                success = {
                    if(it.second) Log.d("YHYH", "위로올리기 - ${it.first}")
                    resetScrollTopState(it.first)
                }
            ).launchIn(initLifecycleOwner().lifecycleScope)
        }
    }
    private fun addNavigationFragment() {
        navigationFragment = NavigationFragment()
        addFragment(R.id.containerNavigation, navigationFragment)
    }
    private fun setNavigationMenu(navigationType: NavigationType) {
        selectMainViewFragment(navigationType)
    }

    private fun setDefaultNavigation() {
        setNavigationMenu(NavigationType.SWIPE)
    }

    private fun selectMainViewFragment(navigationType: NavigationType) {
        // 기존 프래그먼트 숨기기
        hideFragment(onTopFragment)

        when(navigationType) {
            NavigationType.SWIPE -> {
                if(swipeFragment == null) {
                    swipeFragment = SwipeFragment()
                    onTopFragment = swipeFragment
                    addFragment(R.id.containerMain, onTopFragment)
                } else {
                    showFragment(swipeFragment)
                    onTopFragment = swipeFragment
                }
            }
            NavigationType.LIKE -> {
                if(likeFragment == null) {
                    likeFragment = LikeFragment()
                    onTopFragment = likeFragment
                    addFragment(R.id.containerMain, onTopFragment)
                } else {
                    showFragment(likeFragment)
                    onTopFragment = likeFragment
                }
            }
            NavigationType.MESSAGE -> {
                if(messageListFragment == null) {
                    messageListFragment = MessageListFragment()
                    onTopFragment = messageListFragment
                    addFragment(R.id.containerMain, onTopFragment)
                } else {
                    showFragment(messageListFragment)
                    onTopFragment = messageListFragment
                }
            }
            NavigationType.MYPAGE -> {
                if(myPageFragment == null) {
                    myPageFragment = MyPageFragment()
                    onTopFragment = myPageFragment
                    addFragment(R.id.containerMain, onTopFragment)
                } else {
                    showFragment(myPageFragment)
                    onTopFragment = myPageFragment
                }
            }
        }
    }
}