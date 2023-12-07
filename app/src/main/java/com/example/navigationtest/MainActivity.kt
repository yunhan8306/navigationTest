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
    }

    override fun onStart() {
        super.onStart()
        addNavigationFragment()
//        selectSwipeFragment()
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
                    Log.d("YHYH", "위로올리기")
                }
            ).launchIn(initLifecycleOwner().lifecycleScope)
        }
    }


    private fun addNavigationFragment() {
        navigationFragment = NavigationFragment()

        addFragment(R.id.containerNavigation, navigationFragment)

    }

    private fun selectSwipeFragment() {
        selectMainViewFragment(NavigationType.SWIPE)
    }

    private fun selectLikeFragment() {
        selectMainViewFragment(NavigationType.LIKE)
    }

    private fun selectMessageListFragment() {
        selectMainViewFragment(NavigationType.MESSAGE)
    }

    private fun selectMyPageFragment() {
        selectMainViewFragment(NavigationType.MYPAGE)
    }

    private fun selectMainViewFragment(navigationType: NavigationType) {
        when(navigationType) {
            NavigationType.SWIPE -> {
                if(swipeFragment == null) swipeFragment = SwipeFragment()
                onTopFragment = swipeFragment
            }
            NavigationType.LIKE -> {
                if(likeFragment == null) likeFragment = LikeFragment()
                onTopFragment = likeFragment
            }
            NavigationType.MESSAGE -> {
                if(messageListFragment == null) messageListFragment = MessageListFragment()
                onTopFragment = messageListFragment
            }
            NavigationType.MYPAGE -> {
                if(myPageFragment == null) myPageFragment = MyPageFragment()
                onTopFragment = myPageFragment
            }
        }
        addFragment(R.id.containerMain, onTopFragment)
    }
}