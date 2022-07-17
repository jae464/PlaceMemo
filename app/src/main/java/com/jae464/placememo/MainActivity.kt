package com.jae464.placememo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
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
        replaceFragment(HomeFragment())
        initNavigation()
        initListener()
    }

    private fun initListener() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.feed -> {
                    replaceFragment(FeedFragment())
                    true
                }
                R.id.mypage -> {
                    replaceFragment(MyPageFragment())
                    true
                }
                R.id.settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun initNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.nav_host_fragment, fragment)
                commit()
            }
    }
}