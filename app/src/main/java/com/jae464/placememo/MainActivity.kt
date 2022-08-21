package com.jae464.placememo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.databinding.ActivityMainBinding
import com.jae464.placememo.presentation.feed.FeedFragment
import com.jae464.placememo.presentation.home.HomeFragment
import com.jae464.placememo.presentation.mypage.MyPageFragment
import com.jae464.placememo.presentation.settings.SettingsFragment
import com.naver.maps.map.NaverMapSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this
        initNavigation()
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener {_,_,arguments ->
            // PostPage 에서는 BottomNavigationView 안보이기
            binding.bottomNavigationView.isVisible =
                (arguments?.getBoolean("ShowBottomNavigationView", true) ?: true) == true
        }
    }
}