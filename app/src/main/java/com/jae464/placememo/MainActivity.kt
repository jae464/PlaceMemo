package com.jae464.placememo

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jae464.placememo.databinding.ActivityMainBinding
import com.jae464.placememo.presentation.base.BaseActivity

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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