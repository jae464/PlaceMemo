package com.jae464.presentation

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.jae464.presentation.base.BaseActivity
import com.jae464.presentation.databinding.ActivityMainBinding


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

    // EditText 에서 키보드 올라왔을때, 다른 화면 클릭 시 키보드 내리는 메소드
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val view = currentFocus

        if (view != null && (ev?.action == MotionEvent.ACTION_UP || ev?.action == MotionEvent.ACTION_MOVE) && view is EditText) {
            val scrcoords = IntArray(2)

            view.getLocationOnScreen(scrcoords)

            val x = ev.rawX + view.left - scrcoords[0]
            val y = ev.rawY + view.top - scrcoords[1]

            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    (this.window.decorView.applicationWindowToken), 0)
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}